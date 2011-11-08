package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.utils.ScreenNavigationUtil;
import org.openlegacy.terminal.utils.SimpleScreenEntityFieldAccessor;
import org.springframework.context.ApplicationContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
public class DefaultScreenEntityBinder implements ScreenEntityBinder {

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private List<ScreenEntityDataInjector> screenEntityDataInjectors;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityBinder.class);

	@SuppressWarnings("unchecked")
	public <T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			ScreenEntityNotAccessibleException {

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);

		ScreenNavigationUtil.validateCurrentScreen(screenEntity, matchedScreenEntity);

		return (T)buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	public Object buildScreenEntity(TerminalScreen terminalScreen) {
		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		return buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	private Object buildScreenEntityInner(Class<?> screenEntityClass, TerminalScreen terminalScreen) {
		Object screenEntity = applicationContext.getBean(screenEntityClass);

		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);

		fieldAccessor.setTerminalScreen(terminalScreen);

		for (ScreenEntityDataInjector screenEntityDataInjector : screenEntityDataInjectors) {
			screenEntityDataInjector.inject(fieldAccessor, screenEntityClass, terminalScreen);
		}

		return screenEntity;

	}

	public List<TerminalField> buildSendFields(TerminalScreen terminalScreen, Object screenEntity) {
		List<TerminalField> modifiedfields = new ArrayList<TerminalField>();

		if (screenEntity == null) {
			return modifiedfields;
		}

		Collection<FieldMappingDefinition> fieldsInfo = fieldMappingsProvider.getFieldsMappingDefinitions(terminalScreen,
				screenEntity.getClass());

		if (fieldsInfo == null) {
			return modifiedfields;
		}

		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);
		for (FieldMappingDefinition fieldMapping : fieldsInfo) {

			if (fieldMapping.isEditable()) {
				Object value = fieldAccessor.getFieldValue(fieldMapping.getName());
				ScreenPosition screenPosition = fieldMapping.getScreenPosition();

				TerminalField terminalField = terminalScreen.getField(screenPosition);
				if (value != null) {
					terminalField.setValue(value.toString());
					modifiedfields.add(terminalField);
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format(
								"Field {0} was set with value \"{1}\" to send fields for screen entity {2}",
								fieldMapping.getName(), value, screenEntity));
					}
				}
			}

		}
		return modifiedfields;
	}
}
