/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.loaders.RegistryLoader;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.utils.TypesUtil;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultRegistryLoader implements RegistryLoader {

	private final static Log logger = LogFactory.getLog(DefaultRegistryLoader.class);

	private Collection<FieldAnnotationsLoader> fieldAnnotationLoaders;
	private Collection<FieldLoader> fieldLoaders;

	private List<ClassAnnotationsLoader> classAnnotationLoaders;

	private ConfigurableListableBeanFactory beanFactory;

	public void load(EntitiesRegistry<?, ?, ?> entitiesRegistry) {
		entitiesRegistry.clear();

		Assert.notNull(classAnnotationLoaders);
		Assert.notNull(fieldAnnotationLoaders);
		Assert.notNull(fieldLoaders);

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

				// scan all classes with annotations
				for (Resource resource : resources) {
					Class<?> beanClass = getClassFromResource(packagePath, resource);
					loadSingleClass(entitiesRegistry, beanClass, false);
				}
				// another cycle on all classes for scanning fields of registered types (List<TableRowClass> for example)
				for (Resource resource : resources) {
					Class<?> beanClass = getClassFromResource(packagePath, resource);
					handleEntityFields(fieldLoaders, entitiesRegistry, beanClass);
				}

			} catch (Exception e) {
				throw (new RuntimeException(e));
			}
		}

		fillEntitiesReferences(entitiesRegistry);
	}

	public void loadSingleClass(EntitiesRegistry<?, ?, ?> entitiesRegistry, Class<?> beanClass, Boolean loadReferences) {

		if (org.openlegacy.utils.ClassUtils.isAbstract(beanClass)) {
			return;
		}
		if (entitiesRegistry.contains(beanClass)) {
			// class is reloaded - define the registry as dirty for designtime usage
			((AbstractEntitiesRegistry<?, ?, ?>)entitiesRegistry).setDirty(true);
		}
		Class<?> currentBeanClass = beanClass;
		// scan parent classes for annotations
		while (currentBeanClass != Object.class) {
			Annotation[] annotations = currentBeanClass.getAnnotations();
			processAnnotations(classAnnotationLoaders, entitiesRegistry, beanClass, annotations);
			currentBeanClass = currentBeanClass.getSuperclass();
		}
		// scan field for annotations
		handleEntityAnnotationFields(fieldAnnotationLoaders, entitiesRegistry, beanClass);
		handleEntityFields(fieldLoaders, entitiesRegistry, beanClass);

		if (loadReferences) {
			fillEntityReferences(entitiesRegistry, entitiesRegistry.get(beanClass));
		}
	}

	public void setAnnotationLoaders(Collection<ClassAnnotationsLoader> classAnnotationLoaders) {
		this.classAnnotationLoaders = sortClassAnnoationLoaders(classAnnotationLoaders);
	}

	public void setFieldAnnotationLoaders(Collection<FieldAnnotationsLoader> fieldAnnotationLoaders) {
		this.fieldAnnotationLoaders = fieldAnnotationLoaders;
	}

	public void setFieldLoaders(Collection<FieldLoader> fieldLoaders) {
		this.fieldLoaders = fieldLoaders;
	}

	private static Class<?> getClassFromResource(String packagePath, Resource resource) throws IOException,
			ClassNotFoundException {
		String resourcePath = resource.getURI().toString();
		String resourceRelativePath = resourcePath.substring(resourcePath.indexOf(packagePath), resourcePath.indexOf(".class"));
		String className = ClassUtils.convertResourcePathToClassName(resourceRelativePath);

		Class<?> beanClass = Class.forName(className);
		return beanClass;
	}

	/**
	 * fills references to other entities
	 * 
	 * @param entitiesRegistry
	 */
	private static void fillEntitiesReferences(final EntitiesRegistry<?, ?, ?> entitiesRegistry) {
		@SuppressWarnings("unchecked")
		Collection<EntityDefinition<?>> entities = (Collection<EntityDefinition<?>>)entitiesRegistry.getEntitiesDefinitions();
		for (final EntityDefinition<?> entityDefinition : entities) {
			fillEntityReferences(entitiesRegistry, entityDefinition);
		}
	}

	private static void fillEntityReferences(final EntitiesRegistry<?, ?, ?> entitiesRegistry,
			final EntityDefinition<?> entityDefinition) {
		if (entityDefinition == null) {
			return;
		}
		ReflectionUtils.doWithFields(entityDefinition.getEntityClass(), new FieldCallback() {

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

	private void processAnnotations(List<ClassAnnotationsLoader> annotationLoaders, EntitiesRegistry<?, ?, ?> entitiesRegistry,
			Class<?> rootBeanClass, Annotation[] annotations) {
		for (ClassAnnotationsLoader annotationsLoader : annotationLoaders) {
			if (annotationsLoader instanceof AbstractClassAnnotationLoader) {
				((AbstractClassAnnotationLoader)annotationsLoader).setBeanFactory(beanFactory);
			}
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

	private static void handleEntityAnnotationFields(final Collection<FieldAnnotationsLoader> fieldAnnotationLoadersCollection,
			final EntitiesRegistry<?, ?, ?> entitiesRegistry, final Class<?> beanClass) {
		final List<FieldAnnotationsLoader> fieldAnnotationLoaders = sortFieldAnnoationLoaders(fieldAnnotationLoadersCollection);

		final CounterContainer counterContainer = new CounterContainer();

		ReflectionUtils.doWithFields(beanClass, new FieldCallback() {

			public void doWith(Field field) {
				loadDefinitionFromAnnotations(entitiesRegistry, beanClass, fieldAnnotationLoaders, field);
				counterContainer.counter++;
			}

			private void loadDefinitionFromAnnotations(final EntitiesRegistry<?, ?, ?> entitiesRegistry,
					final Class<?> beanClass, final Collection<FieldAnnotationsLoader> fieldAnnotationLoaders, Field field) {
				Annotation[] annotations = field.getAnnotations();
				for (FieldAnnotationsLoader fieldAnnotationsLoader : fieldAnnotationLoaders) {
					for (Annotation annotation : annotations) {
						if (fieldAnnotationsLoader.match(annotation)) {
							if (logger.isDebugEnabled()) {
								logger.debug(MessageFormat.format(
										"Loading annotation {0} for field {1} in entity {2} into registry",
										annotation.annotationType().getSimpleName(), field.getName(), beanClass));
							}
							fieldAnnotationsLoader.load(entitiesRegistry, field, annotation, beanClass, counterContainer.counter);
						}
					}
				}
			}

		});

	}

	private static void handleEntityFields(final Collection<FieldLoader> fieldLoaders,
			final EntitiesRegistry<?, ?, ?> entitiesRegistry, final Class<?> beanClass) {

		final CounterContainer counterContainer = new CounterContainer();

		ReflectionUtils.doWithFields(beanClass, new FieldCallback() {

			public void doWith(Field field) {
				loadDefinition(entitiesRegistry, beanClass, fieldLoaders, field);
				counterContainer.counter++;
			}

			private void loadDefinition(final EntitiesRegistry<?, ?, ?> entitiesRegistry, final Class<?> beanClass,
					final Collection<FieldLoader> fieldLoaders, Field field) {
				for (FieldLoader fieldLoader : fieldLoaders) {
					if (fieldLoader.match(entitiesRegistry, field)) {
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format("Loading field {0} setting for entity {1} into registry",
									field.getName(), beanClass));
						}
						fieldLoader.load(entitiesRegistry, field, beanClass, counterContainer.counter);
					}
				}
			}

		});
	}

	public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	private static class CounterContainer {

		public int counter = 0;
	}
}
