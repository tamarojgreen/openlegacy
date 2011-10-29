package org.openlegacy.adapter.terminal;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.FetchMode;
import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ChildScreenDefinition;
import org.openlegacy.terminal.ChildScreensDefinitionProvider;
import org.openlegacy.terminal.FieldMappingDefinition;
import org.openlegacy.terminal.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.util.ProxyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
@Scope("sesssion")
// since performing action on terminalSession
public class SimpleScreenEntityBinder implements ScreenEntityBinder {

	protected static final String FIELD_SUFFIX = "Field";

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

	private final static Log logger = LogFactory.getLog(SimpleScreenEntityBinder.class);

	private static final String TERMINAL_SCREEN = "terminalScreen";

	public <T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			HostEntityNotAccessibleException {

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		ScreenSyncValidator.validateCurrentScreen(screenEntity, matchedScreenEntity);

		try {
			T screenEntityInstance = applicationContext.getBean(screenEntity);

			Object realScreenEntityInstance = ProxyUtil.getTargetObject(screenEntityInstance, screenEntity);

			injectTerminalScreen(realScreenEntityInstance, terminalScreen);

			injectFields(realScreenEntityInstance, terminalScreen);

			injectChildScreens(realScreenEntityInstance, terminalScreen);

			return screenEntityInstance;
		} catch (Exception e) {
			throw (new IllegalArgumentException(e));
		}
	}

	private static <T> void injectTerminalScreen(T screenEntityInstance, TerminalScreen hostScreen) throws Exception {
		Field hostScreenField = null;

		hostScreenField = screenEntityInstance.getClass().getDeclaredField(TERMINAL_SCREEN);
		hostScreenField.setAccessible(true);
		hostScreenField.set(screenEntityInstance, hostScreen);
	}

	private static TerminalField extractTerminalField(final TerminalScreen terminalScreen, FieldMappingDefinition fieldMapping) {
		TerminalField terminalField = terminalScreen.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

	private static String formatContent(TerminalField terminalField) {
		// TODO add configuration
		return terminalField.getValue().trim();
	}

	private void injectFields(final Object screenEntityInstance, final TerminalScreen terminalScreen) throws Exception {

		Class<? extends Object> screenEntity = screenEntityInstance.getClass();
		Collection<FieldMappingDefinition> fieldMappingDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity);

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingDefinitions) {
			Field javaSimpleField = screenEntity.getDeclaredField(fieldMappingDefinition.getName());
			Field javaTerminalField = screenEntity.getDeclaredField(fieldMappingDefinition.getName() + FIELD_SUFFIX);

			javaSimpleField.setAccessible(true);

			TerminalField terminalField = extractTerminalField(terminalScreen, fieldMappingDefinition);

			String formattedContent = formatContent(terminalField);

			javaSimpleField.set(screenEntityInstance, formattedContent);

			if (javaTerminalField != null) {
				javaTerminalField.setAccessible(true);
				javaTerminalField.set(screenEntityInstance, terminalField);
			}

			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("Field {0} was set with value \"{1}\"", fieldMappingDefinition.getName(),
						formattedContent));
			}
		}

	}

	private void injectChildScreens(final Object screenEntityInstance, final TerminalScreen terminalScreen) throws Exception {

		Class<? extends Object> screenEntity = screenEntityInstance.getClass();
		Collection<ChildScreenDefinition> childScreenDefinitions = childScreensDefinitionProvider.getChildScreenDefinitions(screenEntity);

		for (ChildScreenDefinition childScreenDefinition : childScreenDefinitions) {

			if (childScreenDefinition.getFetchMode() == FetchMode.LAZY) {
				continue;
			}

			String fieldName = childScreenDefinition.getFieldName();
			Field javaField = screenEntity.getDeclaredField(fieldName);

			javaField.setAccessible(true);

			ScreenBindUtil.populateChildScreenField(terminalSession, javaField.getType(), screenEntityInstance, javaField,
					childScreenDefinition);
		}

	}

	public Map<ScreenPosition, String> buildSendFields(TerminalScreen terminalScreen, Object screenEntityInstance) {
		Map<ScreenPosition, String> fieldValues = new HashMap<ScreenPosition, String>();

		if (screenEntityInstance == null) {
			return fieldValues;
		}

		Collection<FieldMappingDefinition> fieldsInfo = fieldMappingsProvider.getFieldsMappingDefinitions(terminalScreen,
				screenEntityInstance.getClass());

		if (fieldsInfo == null) {
			return fieldValues;
		}

		for (FieldMappingDefinition fieldMapping : fieldsInfo) {
			try {
				PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(screenEntityInstance, fieldMapping.getName());

				Method readMethod = descriptor.getReadMethod();
				if (readMethod != null) {
					Object value = readMethod.invoke(screenEntityInstance);
					ScreenPosition hostScreenPosition = fieldMapping.getScreenPosition();

					if (value != null) {
						fieldValues.put(hostScreenPosition, value.toString());
						if (logger.isDebugEnabled()) {
							logger.debug(MessageFormat.format(
									"Field {0} was set with value \"{1}\" to send fields for screenEntity {2}",
									fieldMapping.getName(), value, screenEntityInstance));
						}
					}
				}
			} catch (Exception e) {
				throw (new IllegalStateException(e));
			}

		}
		return fieldValues;
	}

}
