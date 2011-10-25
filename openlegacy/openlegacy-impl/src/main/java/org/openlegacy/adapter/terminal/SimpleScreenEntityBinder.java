package org.openlegacy.adapter.terminal;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.FieldMapping;
import org.openlegacy.terminal.FieldMappingsProvider;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.springframework.beans.factory.annotation.Autowired;

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
public class SimpleScreenEntityBinder implements ScreenEntityBinder {

	protected static final String FIELD_SUFFIX = "Field";

	@Autowired
	private ScreensRecognizer screensRecognizer;

	@Autowired
	private FieldMappingsProvider fieldMappingsProvider;

	private final static Log logger = LogFactory.getLog(SimpleScreenEntityBinder.class);

	private static final String TERMINAL_SCREEN = "terminalScreen";

	public <T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			HostEntityNotAccessibleException {

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		ScreenSyncValidator.validateCurrentScreen(screenEntity, matchedScreenEntity);

		try {
			T screenEntityInstance = screenEntity.newInstance();

			injectTerminalScreen(screenEntityInstance, terminalScreen);

			injectFields(screenEntityInstance, terminalScreen);
			return screenEntityInstance;
		} catch (Exception e) {
			throw (new IllegalArgumentException(e));
		}
	}

	private static <T> void injectTerminalScreen(T screenEntityInstance, TerminalScreen hostScreen) throws NoSuchFieldException,
			IllegalAccessException {
		Field hostScreenField = screenEntityInstance.getClass().getDeclaredField(TERMINAL_SCREEN);
		hostScreenField.setAccessible(true);
		hostScreenField.set(screenEntityInstance, hostScreen);
	}

	private static TerminalField extractTerminalField(final TerminalScreen terminalScreen, FieldMapping fieldMapping) {
		TerminalField terminalField = terminalScreen.getField(fieldMapping.getScreenPosition());
		return terminalField;
	}

	private static String formatContent(TerminalField terminalField) {
		// TODO add configuration
		return terminalField.getValue().trim();
	}

	private void injectFields(final Object screenEntityInstance, final TerminalScreen terminalScreen)
			throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		Class<? extends Object> screenEntity = screenEntityInstance.getClass();
		Collection<FieldMapping> fieldMappings = fieldMappingsProvider.getFieldsMappings(terminalScreen, screenEntity);

		for (FieldMapping fieldMapping : fieldMappings) {
			Field javaSimpleField = screenEntity.getDeclaredField(fieldMapping.getName());
			Field javaTerminalField = screenEntity.getDeclaredField(fieldMapping.getName() + FIELD_SUFFIX);

			javaSimpleField.setAccessible(true);
			javaTerminalField.setAccessible(true);

			TerminalField terminalField = extractTerminalField(terminalScreen, fieldMapping);

			String formattedContent = formatContent(terminalField);
			javaSimpleField.set(screenEntityInstance, formattedContent);

			javaTerminalField.set(screenEntityInstance, terminalField);

			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format("Field {0} was set with value \"{1}\"", fieldMapping.getName(),
						formattedContent));
			}
		}

	}

	public Map<ScreenPosition, String> buildSendFields(TerminalScreen terminalScreen, Object screenEntityInstance) {
		Map<ScreenPosition, String> fieldValues = new HashMap<ScreenPosition, String>();

		if (screenEntityInstance == null) {
			return fieldValues;
		}

		Collection<FieldMapping> fieldsInfo = fieldMappingsProvider.getFieldsMappings(terminalScreen,
				screenEntityInstance.getClass());

		if (fieldsInfo == null) {
			return fieldValues;
		}

		for (FieldMapping fieldMapping : fieldsInfo) {
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
