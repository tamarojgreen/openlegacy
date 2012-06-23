package org.openlegacy.terminal.support;

import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.openlegacy.loaders.FieldAnnotationsLoader;
import org.openlegacy.loaders.FieldLoader;
import org.openlegacy.loaders.RegistryLoader;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Collection;

/**
 * Open legacy integration point with spring component-scan. The classes are scanned for @ScreenEntity annotation and all the
 * annotations information is extracted and kept in ScreenEntitiesRegistry
 * 
 * @param <T>
 */
public class ScreenAnnotationProccesor<T> implements BeanFactoryPostProcessor {

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<ClassAnnotationsLoader> classAnnotationsLoaders = beanFactory.getBeansOfType(ClassAnnotationsLoader.class).values();
		Collection<FieldAnnotationsLoader> fieldAnnotationLoaders = beanFactory.getBeansOfType(
				FieldAnnotationsLoader.class).values();
		Collection<FieldLoader> fieldLoaders = beanFactory.getBeansOfType(FieldLoader.class).values();

		ScreenEntitiesRegistry screenEntitiesRegistry = beanFactory.getBean(ScreenEntitiesRegistry.class);

		RegistryLoader registryLoader = beanFactory.getBean(RegistryLoader.class);
		registryLoader.load(screenEntitiesRegistry, classAnnotationsLoaders, fieldAnnotationLoaders, fieldLoaders);
	}

}
