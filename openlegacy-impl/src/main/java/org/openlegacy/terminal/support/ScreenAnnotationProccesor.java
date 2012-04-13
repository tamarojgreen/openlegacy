package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.TypesUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Open legacy integration point with spring component-scan. The classes are scanned for @ScreenEntity annotation and all the
 * annotations information is extracted and kept in ScreenEntitiesRegistry
 * 
 * @param <T>
 */
public class ScreenAnnotationProccesor<T> implements BeanFactoryPostProcessor {

	private final static Log logger = LogFactory.getLog(ScreenAnnotationProccesor.class);

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<ClassAnnotationsLoader> annotationLoadersCollection = beanFactory.getBeansOfType(ClassAnnotationsLoader.class).values();
		List<ClassAnnotationsLoader> annotationLoaders = sortClassAnnoationLoaders(annotationLoadersCollection);

		ScreenEntitiesRegistry screenEntitiesRegistry = beanFactory.getBean(ScreenEntitiesRegistry.class);
		screenEntitiesRegistry.clear();

		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			try {
				BeanDefinition bean = beanFactory.getBeanDefinition(beanName);
				if (bean.getBeanClassName() == null) {
					continue;
				}
				Class<?> beanClass = Class.forName(bean.getBeanClassName());

				Class<?> currentBeanClass = beanClass;
				while (currentBeanClass != Object.class) {
					Annotation[] annotations = currentBeanClass.getAnnotations();
					processAnnotations(annotationLoaders, screenEntitiesRegistry, beanClass, annotations);
					currentBeanClass = currentBeanClass.getSuperclass();
				}
				handleChilds(beanFactory, screenEntitiesRegistry, beanClass);
			} catch (ClassNotFoundException e) {
				throw (new BeanCreationException(e.getMessage(), e));
			}
		}

		fillChildScreenEntities(screenEntitiesRegistry);
	}

	private static void fillChildScreenEntities(final ScreenEntitiesRegistry screenEntitiesRegistry) {
		Collection<Class<?>> screenEntities = screenEntitiesRegistry.getAll();
		for (Class<?> class1 : screenEntities) {

			final ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(class1);

			ReflectionUtils.doWithFields(class1, new FieldCallback() {

				public void doWith(Field field) {
					if (TypesUtil.isPrimitive(field.getType())) {
						return;
					}
					if (List.class.isAssignableFrom(field.getType())) {
						return;
					}
					ScreenEntityDefinition childScreenEntity = screenEntitiesRegistry.get(field.getType());
					if (childScreenEntity != null) {
						screenEntityDefinition.getChildScreensDefinitions().add(childScreenEntity);
					}
				}
			});
		}
	}

	private static void processAnnotations(List<ClassAnnotationsLoader> annotationLoaders,
			ScreenEntitiesRegistry screenEntitiesRegistry, Class<?> rootBeanClass, Annotation[] annotations) {
		for (ClassAnnotationsLoader annotationsLoader : annotationLoaders) {
			for (Annotation annotation : annotations) {
				if (annotationsLoader.match(annotation)) {
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format("Loading annotation {0} for screen {1} into registry",
								annotation.annotationType().getSimpleName(), rootBeanClass));
					}
					annotationsLoader.load(screenEntitiesRegistry, annotation, rootBeanClass);

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

	private static void handleChilds(final ConfigurableListableBeanFactory beanFactory,
			final ScreenEntitiesRegistry screenEntitiesRegistry, final Class<?> beanClass) {
		Collection<FieldAnnotationsLoader> fieldAnnotationLoadersCollection = beanFactory.getBeansOfType(
				FieldAnnotationsLoader.class).values();
		final List<FieldAnnotationsLoader> fieldAnnotationLoaders = sortFieldAnnoationLoaders(fieldAnnotationLoadersCollection);

		final Collection<FieldLoader> fieldLoaders = beanFactory.getBeansOfType(FieldLoader.class).values();
		ReflectionUtils.doWithFields(beanClass, new FieldCallback() {

			public void doWith(Field field) {

				loadDefinition(screenEntitiesRegistry, beanClass, fieldLoaders, field);
				loadDefinitionFromAnnotations(screenEntitiesRegistry, beanClass, fieldAnnotationLoaders, field);
			}

			private void loadDefinitionFromAnnotations(final ScreenEntitiesRegistry screenEntitiesRegistry,
					final Class<?> beanClass, final Collection<FieldAnnotationsLoader> fieldAnnotationLoaders, Field field) {
				Annotation[] annotations = field.getAnnotations();
				for (FieldAnnotationsLoader fieldAnnotationsLoader : fieldAnnotationLoaders) {
					for (Annotation annotation : annotations) {
						if (fieldAnnotationsLoader.match(annotation)) {
							if (logger.isDebugEnabled()) {
								logger.debug(MessageFormat.format(
										"Loading annotation {0} for field {1} in screen {2} into registry",
										annotation.annotationType().getSimpleName(), field.getName(), beanClass));
							}
							fieldAnnotationsLoader.load(beanFactory, screenEntitiesRegistry, field, annotation, beanClass);
						}
					}
				}
			}

			private void loadDefinition(final ScreenEntitiesRegistry screenEntitiesRegistry, final Class<?> beanClass,
					final Collection<FieldLoader> fieldLoaders, Field field) {
				for (FieldLoader fieldLoader : fieldLoaders) {
					if (fieldLoader.match(screenEntitiesRegistry, field)) {
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format("Loading field {0} setting for screen {1} into registry",
									field.getName(), beanClass));
						}
						fieldLoader.load(beanFactory, screenEntitiesRegistry, field, beanClass);
					}
				}
			}

		});

	}
}
