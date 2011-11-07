package org.openlegacy.terminal.support;

import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Open legacy integration point with spring component-scan. The classes are scanned for @ScreenEntity annotation and all the
 * annotations information is extracted and kept in ScreenEntitiesRegistry
 * 
 * @param <T>
 */
public class ScreenAnnotationProccesor<T> implements BeanFactoryPostProcessor {

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<ClassAnnotationsLoader> annotationLoaders = beanFactory.getBeansOfType(ClassAnnotationsLoader.class).values();

		ScreenEntitiesRegistry screenEntitiesRegistry = beanFactory.getBean(ScreenEntitiesRegistry.class);
		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			try {
				BeanDefinition bean = beanFactory.getBeanDefinition(beanName);
				Class<?> beanClass = Class.forName(bean.getBeanClassName());

				Annotation[] annotations = beanClass.getAnnotations();
				for (ClassAnnotationsLoader annotationsLoader : annotationLoaders) {
					for (Annotation annotation : annotations) {
						if (annotationsLoader.match(annotation)) {
							annotationsLoader.load(screenEntitiesRegistry, annotation, beanClass);

						}
					}
				}
				handleChilds(beanFactory, screenEntitiesRegistry, beanClass);
			} catch (ClassNotFoundException e) {
				throw (new BeanCreationException(e.getMessage(), e));
			}
		}
	}

	private static void handleChilds(final ConfigurableListableBeanFactory beanFactory,
			final ScreenEntitiesRegistry screenEntitiesRegistry, final Class<?> beanClass) {
		final Collection<FieldAnnotationsLoader> fieldAnnotationLoaders = beanFactory.getBeansOfType(FieldAnnotationsLoader.class).values();
		final Collection<FieldLoader> fieldLoaders = beanFactory.getBeansOfType(FieldLoader.class).values();
		ReflectionUtils.doWithFields(beanClass, new FieldCallback() {

			public void doWith(Field field) {

				loadDefinition(screenEntitiesRegistry, beanClass, fieldLoaders, field);
				loadDefinitionFromAnnotations(screenEntitiesRegistry, beanClass, fieldAnnotationLoaders, field);
			}

			private void loadDefinitionFromAnnotations(final ScreenEntitiesRegistry screenEntitiesRegistry,
					final Class<?> beanClass, final Collection<FieldAnnotationsLoader> fieldAnnotationLoaders, Field field) {
				Annotation[] annotations = field.getAnnotations();
				for (Annotation annotation : annotations) {
					for (FieldAnnotationsLoader fieldAnnotationsLoader : fieldAnnotationLoaders) {
						if (fieldAnnotationsLoader.match(annotation)) {
							fieldAnnotationsLoader.load(screenEntitiesRegistry, field.getName(), annotation, beanClass);
						}
					}
				}
			}

			private void loadDefinition(final ScreenEntitiesRegistry screenEntitiesRegistry, final Class<?> beanClass,
					final Collection<FieldLoader> fieldLoaders, Field field) {
				for (FieldLoader fieldLoader : fieldLoaders) {
					if (fieldLoader.match(screenEntitiesRegistry, field)) {
						fieldLoader.load(screenEntitiesRegistry, field, beanClass);
					}
				}
			}

		});

	}
}
