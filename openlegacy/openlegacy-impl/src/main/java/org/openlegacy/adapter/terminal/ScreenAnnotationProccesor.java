package org.openlegacy.adapter.terminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.terminal.FieldMapping;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
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
public class ScreenAnnotationProccesor<T> implements BeanPostProcessor {

	@Autowired
	private ScreenEntitiesRegistry screensRegistry;

	private final static Log logger = LogFactory.getLog(ScreenAnnotationProccesor.class);

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanClass = bean.getClass();

		ScreenEntity screenEntity = AnnotationUtils.findAnnotation(beanClass, ScreenEntity.class);
		if (screenEntity != null) {
			String screenName = screenEntity.name().length() > 0 ? screenEntity.name() : beanClass.getSimpleName();
			screensRegistry.add(screenName, beanClass);
			logger.info(MessageFormat.format("Screen \"{0}\" was added to the screen registry ({1})", screenName,
					beanClass.getName()));
			addIdentifiers(beanClass, screenName, screenEntity);

			addFieldMappings(beanClass, screenName, screenEntity);
		}
		return bean;
	}

	private void addIdentifiers(Class<? extends Object> beanClass, String screenEntityName, ScreenEntity screenEntity) {
		if (screenEntity.identifiers().length > 0) {
			Identifier[] identifiers = screenEntity.identifiers();
			SimpleScreenIdentification screenIdentification = new SimpleScreenIdentification(screenEntityName);
			for (Identifier identifier : identifiers) {
				ScreenPosition position = ScreenPosition.newInstance(identifier.row(), identifier.column());
				String text = identifier.value();
				SimpleScreenIdentifier simpleIdentifier = new SimpleScreenIdentifier(position, text);
				screenIdentification.addIdentifier(simpleIdentifier);

				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Identifier {0} - \"{1}\" was added to the repository for screen {2}",
							position, text, screenIdentification.getName()));
				}

			}
			screensRegistry.addScreenIdentification(screenIdentification);
			logger.info(MessageFormat.format("Screen identification for \"{0}\" was added to the screen registry",
					screenIdentification.getName()));
		}
	}

	private void addFieldMappings(final Class<?> beanClass, String screenName, ScreenEntity hostEntity) {
		ReflectionUtils.doWithFields(beanClass, new FieldCallback() {

			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

				if (field.isAnnotationPresent(org.openlegacy.annotations.screen.FieldMapping.class)) {
					screensRegistry.addFieldMapping(beanClass, extractFieldMapping(field));
				}
			}

		});

	}

	private FieldMapping extractFieldMapping(Field field) {
		org.openlegacy.annotations.screen.FieldMapping fieldAnnotation = field.getAnnotation(org.openlegacy.annotations.screen.FieldMapping.class);

		return new FieldMapping(field.getName(), ScreenPosition.newInstance(fieldAnnotation.row(), fieldAnnotation.column()),
				fieldAnnotation.length());

	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
