package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FetchMode;
import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ChildScreensDefinitionProvider;
import org.openlegacy.terminal.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ChildScreenDefinition;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.utils.ScreenAccessUtils;
import org.openlegacy.terminal.utils.ScreenEntityDirectFieldAccessor;
import org.openlegacy.terminal.utils.ScreenSyncValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
@Scope("sesssion")
// since performing action on terminalSession
public class DefaultScreenEntityBinder implements ScreenEntityBinder {

	@Autowired
	private ScreensRecognizer screensRecognizer;

	@Autowired
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	@Autowired
	private ChildScreensDefinitionProvider childScreensDefinitionProvider;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TerminalSession terminalSession;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityBinder.class);

	private static final String TERMINAL_SCREEN = "terminalScreen";

	@SuppressWarnings("unchecked")
	public <T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			HostEntityNotAccessibleException {

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		ScreenSyncValidator.validateCurrentScreen(screenEntity, matchedScreenEntity);

		return (T)buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	public Object buildScreenEntity(TerminalScreen terminalScreen) {
		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		return buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	private Object buildScreenEntityInner(Class<?> screenEntityClass, TerminalScreen terminalScreen) {
		try {
			Object screenEntity = applicationContext.getBean(screenEntityClass);

			ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(screenEntity);

			fieldAccessor.setFieldValue(TERMINAL_SCREEN, terminalScreen);

			injectFields(fieldAccessor, screenEntityClass, terminalScreen);

			injectChildScreens(fieldAccessor, screenEntityClass, terminalScreen);

			return screenEntity;
		} catch (Exception e) {
			throw (new IllegalArgumentException(e));
		}

	}

	private static TerminalField extractTerminalField(final TerminalScreen terminalScreen, FieldMappingDefinition fieldMapping) {
		TerminalField terminalField = terminalScreen.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

	private void injectFields(ScreenEntityDirectFieldAccessor fieldAccessor, Class<?> screenEntity,
			final TerminalScreen terminalScreen) throws Exception {

		Collection<FieldMappingDefinition> fieldMappingDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity);

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingDefinitions) {

			TerminalField terminalField = extractTerminalField(terminalScreen, fieldMappingDefinition);

			fieldAccessor.setTerminalField(fieldMappingDefinition.getName(), terminalField);

		}

	}

	private void injectChildScreens(ScreenEntityDirectFieldAccessor fieldAccessor, Class<?> screenEntity,
			final TerminalScreen terminalScreen) throws Exception {

		Collection<ChildScreenDefinition> childScreenDefinitions = childScreensDefinitionProvider.getChildScreenDefinitions(screenEntity);

		for (ChildScreenDefinition childScreenDefinition : childScreenDefinitions) {

			if (childScreenDefinition.getFetchMode() == FetchMode.LAZY) {
				continue;
			}

			String fieldName = childScreenDefinition.getFieldName();

			Class<?> fieldType = fieldAccessor.getFieldType(fieldName);
			Object childScreen = ScreenAccessUtils.getChildScreen(terminalSession, fieldType, childScreenDefinition);

			fieldAccessor.setFieldValue(fieldName, childScreen);
		}

	}

	public Map<ScreenPosition, String> buildSendFields(TerminalScreen terminalScreen, Object screenEntity) {
		Map<ScreenPosition, String> fieldValues = new LinkedHashMap<ScreenPosition, String>();

		if (screenEntity == null) {
			return fieldValues;
		}

		Collection<FieldMappingDefinition> fieldsInfo = fieldMappingsProvider.getFieldsMappingDefinitions(terminalScreen,
				screenEntity.getClass());

		if (fieldsInfo == null) {
			return fieldValues;
		}

		ScreenEntityDirectFieldAccessor fieldAccessor = new ScreenEntityDirectFieldAccessor(screenEntity);
		for (FieldMappingDefinition fieldMapping : fieldsInfo) {

			if (fieldMapping.isEditable()) {
				Object value = fieldAccessor.getFieldValue(fieldMapping.getName());
				ScreenPosition hostScreenPosition = fieldMapping.getScreenPosition();

				if (value != null) {
					fieldValues.put(hostScreenPosition, value.toString());
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format(
								"Field {0} was set with value \"{1}\" to send fields for screenEntity {2}",
								fieldMapping.getName(), value, screenEntity));
					}
				}
			}

		}
		return fieldValues;
	}

}
