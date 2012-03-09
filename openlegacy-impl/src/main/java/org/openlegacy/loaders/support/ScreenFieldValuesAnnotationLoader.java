package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.terminal.ScreenRecordsProvider;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.utils.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

import javax.inject.Inject;

@Component
public class ScreenFieldValuesAnnotationLoader extends AbstractFieldAnnotationLoader implements ApplicationContextAware {

	@Inject
	private ApplicationContext applicationContext;

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenFieldValues.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(BeanFactory beanFactory, EntitiesRegistry entitiesRegistry, String fieldName, Annotation annotation,
			Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		ScreenFieldValues fieldValuesAnnotation = (ScreenFieldValues)annotation;

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(containingClass);
		// look in screen entities
		if (screenEntityDefinition != null) {
			SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinition.getFieldsDefinitions().get(
					fieldName);
			setRecordProviderDefinitions(fieldValuesAnnotation, fieldDefinition);

		} else {
			// look in screen entities parts
			ScreenPartEntityDefinition screenPart = screenEntitiesRegistry.getPart(containingClass);
			if (screenPart != null) {
				fieldName = MessageFormat.format("{0}.{1}", screenPart.getPartName(), fieldName);
				SimpleScreenFieldDefinition fieldDefinition = (SimpleScreenFieldDefinition)screenPart.getFieldsDefinitions().get(
						fieldName);
				setRecordProviderDefinitions(fieldValuesAnnotation, fieldDefinition);
			}

		}

	}

	private void setRecordProviderDefinitions(ScreenFieldValues fieldValuesAnnotation, SimpleScreenFieldDefinition fieldDefinition) {
		ScreenRecordsProvider screenRecordsProvider = SpringUtil.getDefaultBean(applicationContext,
				fieldValuesAnnotation.provider());
		fieldDefinition.setRecordsProvider(screenRecordsProvider);
		fieldDefinition.setSourceScreenEntityClass(fieldValuesAnnotation.sourceScreenEntity());
		fieldDefinition.setCollectAllRecords(fieldValuesAnnotation.collectAll());
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
