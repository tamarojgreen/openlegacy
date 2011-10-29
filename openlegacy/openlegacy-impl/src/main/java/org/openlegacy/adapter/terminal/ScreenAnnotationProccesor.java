package org.openlegacy.adapter.terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.annotations.screen.ChildScreenEntity;
import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.terminal.ChildScreenDefinition;
import org.openlegacy.terminal.FieldMappingDefinition;
import org.openlegacy.terminal.ScreenEntityDefinition;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.SimpleChildScreenDefinition;
import org.openlegacy.terminal.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * Open legacy integration point with spring component-scan. The classes are scanned for @ScreenEntity annotation and all the
 * annotations information is extracted and kept in ScreenEntitiesRegistry
 * 
 * @param <T>
 */
public class ScreenAnnotationProccesor<T> implements BeanFactoryPostProcessor {

	@Autowired
	private ScreenEntitiesRegistry screensRegistry;

	private final static Log logger = LogFactory.getLog(ScreenAnnotationProccesor.class);

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		screensRegistry = beanFactory.getBean(ScreenEntitiesRegistry.class);
		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			try {
				BeanDefinition bean = beanFactory.getBeanDefinition(beanName);
				Class<?> beanClass = Class.forName(bean.getBeanClassName());

				ScreenEntity screenEntity = AnnotationUtils.findAnnotation(beanClass, ScreenEntity.class);
				if (screenEntity != null) {
					processScreenEntity(beanClass, screenEntity);
				}
			} catch (ClassNotFoundException e) {
				throw (new BeanCreationException(e.getMessage(), e));
			}
		}
	}

	public void processScreenEntity(Class<?> screenEntityClass, ScreenEntity screenEntity) {
		String screenName = screenEntity.name().length() > 0 ? screenEntity.name() : screenEntityClass.getSimpleName();
		SimpleScreenEntityDefinition screenEntityDefinition = new SimpleScreenEntityDefinition(screenName, screenEntityClass);
		screenEntityDefinition.setName(screenName);

		logger.info(MessageFormat.format("Screen \"{0}\" was added to the screen registry ({1})", screenName,
				screenEntityClass.getName()));
		addIdentifiers(screenEntityDefinition, screenEntity);

		addFieldMappingDefinitions(screenEntityDefinition, screenEntity);

		addChildScreenDefinitions(screenEntityDefinition, screenEntity);
		screensRegistry.add(screenEntityDefinition);
	}

	private static void addIdentifiers(SimpleScreenEntityDefinition screenEntityDefinition, ScreenEntity screenEntity) {
		if (screenEntity.identifiers().length > 0) {
			Identifier[] identifiers = screenEntity.identifiers();
			SimpleScreenIdentification screenIdentification = new SimpleScreenIdentification();
			for (Identifier identifier : identifiers) {
				ScreenPosition position = SimpleScreenPosition.newInstance(identifier.row(), identifier.column());
				String text = identifier.value();
				SimpleScreenIdentifier simpleIdentifier = new SimpleScreenIdentifier(position, text);
				screenIdentification.addIdentifier(simpleIdentifier);

				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Identifier {0} - \"{1}\" was added to the registry for screen {2}",
							position, text, screenEntityDefinition.getHostEntityClass()));
				}

			}
			screenEntityDefinition.setScreenIdentification(screenIdentification);
			logger.info(MessageFormat.format("Screen identifications for \"{0}\" was added to the screen registry",
					screenEntityDefinition.getHostEntityClass()));
		}
	}

	private static void addFieldMappingDefinitions(final ScreenEntityDefinition screenEntityDefinition, ScreenEntity hostEntity) {
		ReflectionUtils.doWithFields(screenEntityDefinition.getHostEntityClass(), new FieldCallback() {

			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

				if (field.isAnnotationPresent(FieldMapping.class)) {
					screenEntityDefinition.getFieldMappingDefinitions().put(field.getName(), extractFieldMappingDefinition(field));
				}
			}
		});

	}

	private static FieldMappingDefinition extractFieldMappingDefinition(Field field) {
		FieldMapping fieldAnnotation = field.getAnnotation(FieldMapping.class);

		return new FieldMappingDefinition(field.getName(), SimpleScreenPosition.newInstance(fieldAnnotation.row(),
				fieldAnnotation.column()), fieldAnnotation.length());

	}

	private static void addChildScreenDefinitions(final SimpleScreenEntityDefinition screenEntityDefinition,
			ScreenEntity screenEntity) {
		ReflectionUtils.doWithFields(screenEntityDefinition.getHostEntityClass(), new FieldCallback() {

			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

				if (field.isAnnotationPresent(ChildScreenEntity.class)) {
					screenEntityDefinition.getChildScreenDefinitions().put(field.getName(), extractChildScreenDefinition(field));
				}
			}
		});
	}

	private static ChildScreenDefinition extractChildScreenDefinition(Field field) {
		ChildScreenEntity childScreenEntityAnnotation = field.getAnnotation(ChildScreenEntity.class);

		SimpleChildScreenDefinition simpleChildScreenDefinition = new SimpleChildScreenDefinition(field.getName());
		simpleChildScreenDefinition.setFetchMode(childScreenEntityAnnotation.fetchMode());
		simpleChildScreenDefinition.setStepInto(childScreenEntityAnnotation.stepInto());
		return simpleChildScreenDefinition;
	}
}
