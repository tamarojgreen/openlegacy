package org.openlegacy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.loaders.RegistryLoader;
import org.openlegacy.utils.TypesUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultRegistryLoader<T> implements RegistryLoader {

	private final static Log logger = LogFactory.getLog(DefaultRegistryLoader.class);

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Collection<ClassAnnotationsLoader> annotationLoaders,
			Collection<FieldAnnotationsLoader> fieldAnnotationLoaders, Collection<FieldLoader> fieldLoaders) {
		List<ClassAnnotationsLoader> annotationLoadersList = sortClassAnnoationLoaders(annotationLoaders);

		entitiesRegistry.clear();

		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources;
		List<String> packages = entitiesRegistry.getPackages();
		if (packages == null) {
			logger.warn("Not packages defined for entity registry " + entitiesRegistry.getClass());
			return;
		}

		for (String thePackage : packages) {
			try {
				String packagePath = org.openlegacy.utils.ClassUtils.packageToResourcePath(thePackage);
				resources = resourcePatternResolver.getResources("classpath*:" + packagePath + "/*.class");

				for (Resource resource : resources) {
					String resourcePath = resource.getURI().toString();
					String resourceRelativePath = resourcePath.substring(resourcePath.indexOf(packagePath),
							resourcePath.indexOf(".class"));
					String className = ClassUtils.convertResourcePathToClassName(resourceRelativePath);

					Class<?> beanClass = Class.forName(className);
					if (org.openlegacy.utils.ClassUtils.isAbstract(beanClass)) {
						continue;
					}
					Class<?> currentBeanClass = Class.forName(className);
					while (currentBeanClass != Object.class) {
						Annotation[] annotations = currentBeanClass.getAnnotations();
						processAnnotations(annotationLoadersList, entitiesRegistry, beanClass, annotations);
						currentBeanClass = currentBeanClass.getSuperclass();
					}
					handleChilds(fieldAnnotationLoaders, fieldLoaders, entitiesRegistry, beanClass);
				}

			} catch (Exception e) {
				throw (new RuntimeException(e));
			}
		}

		fillChildEntities(entitiesRegistry);
	}

	private static void fillChildEntities(final EntitiesRegistry<?, ?> entitiesRegistry) {
		@SuppressWarnings("unchecked")
		Collection<Class<?>> entities = (Collection<Class<?>>)entitiesRegistry.getEntitiesDefinitions();
		for (Class<?> class1 : entities) {

			final EntityDefinition<?> entityDefinition = entitiesRegistry.get(class1);

			ReflectionUtils.doWithFields(class1, new FieldCallback() {

				public void doWith(Field field) {
					if (TypesUtil.isPrimitive(field.getType())) {
						return;
					}
					if (List.class.isAssignableFrom(field.getType())) {
						return;
					}
					EntityDefinition<?> childEntity = entitiesRegistry.get(field.getType());
					if (childEntity != null) {
						entityDefinition.getChildEntitiesDefinitions().add(childEntity);
					}
				}
			});
		}
	}

	private static void processAnnotations(List<ClassAnnotationsLoader> annotationLoaders,
			EntitiesRegistry<?, ?> entitiesRegistry, Class<?> rootBeanClass, Annotation[] annotations) {
		for (ClassAnnotationsLoader annotationsLoader : annotationLoaders) {
			for (Annotation annotation : annotations) {
				if (annotationsLoader.match(annotation)) {
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Loading annotation {0} for entity {1} into registry",
								annotation.annotationType().getSimpleName(), rootBeanClass));
					}
					annotationsLoader.load(entitiesRegistry, annotation, rootBeanClass);

				}
			}
		}
	}

	private static List<ClassAnnotationsLoader> sortClassAnnoationLoaders(
			Collection<ClassAnnotationsLoader> annotationLoadersCollection) {
		List<ClassAnnotationsLoader> annotationLoaders = new ArrayList<ClassAnnotationsLoader>(annotationLoadersCollection);
		Collections.sort(annotationLoaders);
		return annotationLoaders;
	}

	private static List<FieldAnnotationsLoader> sortFieldAnnoationLoaders(
			Collection<FieldAnnotationsLoader> annotationLoadersCollection) {
		List<FieldAnnotationsLoader> annotationLoaders = new ArrayList<FieldAnnotationsLoader>(annotationLoadersCollection);
		Collections.sort(annotationLoaders);
		return annotationLoaders;
	}

	private static void handleChilds(final Collection<FieldAnnotationsLoader> fieldAnnotationLoadersCollection,
			final Collection<FieldLoader> fieldLoaders, final EntitiesRegistry<?, ?> entitiesRegistry, final Class<?> beanClass) {
		final List<FieldAnnotationsLoader> fieldAnnotationLoaders = sortFieldAnnoationLoaders(fieldAnnotationLoadersCollection);

		ReflectionUtils.doWithFields(beanClass, new FieldCallback() {

			public void doWith(Field field) {

				loadDefinition(entitiesRegistry, beanClass, fieldLoaders, field);
				loadDefinitionFromAnnotations(entitiesRegistry, beanClass, fieldAnnotationLoaders, field);
			}

			private void loadDefinitionFromAnnotations(final EntitiesRegistry<?, ?> entitiesRegistry, final Class<?> beanClass,
					final Collection<FieldAnnotationsLoader> fieldAnnotationLoaders, Field field) {
				Annotation[] annotations = field.getAnnotations();
				for (FieldAnnotationsLoader fieldAnnotationsLoader : fieldAnnotationLoaders) {
					for (Annotation annotation : annotations) {
						if (fieldAnnotationsLoader.match(annotation)) {
							if (logger.isDebugEnabled()) {
								logger.debug(MessageFormat.format(
										"Loading annotation {0} for field {1} in entity {2} into registry",
										annotation.annotationType().getSimpleName(), field.getName(), beanClass));
							}
							fieldAnnotationsLoader.load(entitiesRegistry, field, annotation, beanClass);
						}
					}
				}
			}

			private void loadDefinition(final EntitiesRegistry<?, ?> entitiesRegistry, final Class<?> beanClass,
					final Collection<FieldLoader> fieldLoaders, Field field) {
				for (FieldLoader fieldLoader : fieldLoaders) {
					if (fieldLoader.match(entitiesRegistry, field)) {
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format("Loading field {0} setting for entity {1} into registry",
									field.getName(), beanClass));
						}
						fieldLoader.load(entitiesRegistry, field, beanClass);
					}
				}
			}

		});

	}
}
