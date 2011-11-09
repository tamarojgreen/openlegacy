package org.openlegacy.terminal.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.HostAction;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.HostActionMapper;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.exceptions.SendActionException;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.spi.TerminalSendAction;
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

	@Inject
	private HostActionMapper hostActionMapper;

	private final static Log logger = LogFactory.getLog(DefaultScreenEntityBinder.class);

	@SuppressWarnings("unchecked")
	public <T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			ScreenEntityNotAccessibleException {

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);

		ScreenNavigationUtil.validateCurrentScreen(screenEntity, matchedScreenEntity);

		return (T)buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	public <S extends ScreenEntity> S buildScreenEntity(TerminalScreen terminalScreen) {
		Class<?> matchedScreenEntity = screensRecognizer.match(terminalScreen);
		return buildScreenEntityInner(matchedScreenEntity, terminalScreen);
	}

	private <S extends ScreenEntity> S buildScreenEntityInner(Class<?> screenEntityClass, TerminalScreen terminalScreen) {
		@SuppressWarnings("unchecked")
		S screenEntity = (S)applicationContext.getBean(screenEntityClass);

		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);

		fieldAccessor.setTerminalScreen(terminalScreen);

		for (ScreenEntityDataInjector screenEntityDataInjector : screenEntityDataInjectors) {
			screenEntityDataInjector.inject(fieldAccessor, screenEntityClass, terminalScreen);
		}

		return screenEntity;

	}

	public TerminalSendAction buildSendFields(TerminalScreen terminalScreen, HostAction hostAction, ScreenEntity screenEntity) {
		List<TerminalField> modifiedfields = new ArrayList<TerminalField>();

		SimpleTerminalSendAction sendAction = new SimpleTerminalSendAction(modifiedfields,
				hostActionMapper.getCommand(hostAction.getClass()), null);

		if (screenEntity == null) {
			return sendAction;
		}

		Collection<FieldMappingDefinition> fieldMappingsDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity.getClass());

		if (fieldMappingsDefinitions == null) {
			return sendAction;
		}

		String focusField = screenEntity.getFocusField();

		ScreenEntityFieldAccessor fieldAccessor = new SimpleScreenEntityFieldAccessor(screenEntity);

		for (FieldMappingDefinition fieldMappingDefinition : fieldMappingsDefinitions) {

			ScreenPosition cursorPosition = fieldMappingDefinition.getScreenPosition();
			if (fieldMappingDefinition.isEditable()) {
				Object value = fieldAccessor.getFieldValue(fieldMappingDefinition.getName());
				ScreenPosition screenPosition = cursorPosition;

				TerminalField terminalField = terminalScreen.getField(screenPosition);
				if (value != null) {
					terminalField.setValue(value.toString());
					modifiedfields.add(terminalField);
					if (logger.isDebugEnabled()) {
						logger.debug(MessageFormat.format(
								"Field {0} was set with value \"{1}\" to send fields for screen entity {2}",
								fieldMappingDefinition.getName(), value, screenEntity));
					}
				}
			}
			if (fieldMappingDefinition.getName().equals(focusField)) {
				sendAction.setCursorPosition(cursorPosition);
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Cursor was set at position {0} from field {1}", cursorPosition, focusField));
				}
			}

		}

		if (!StringUtils.isEmpty(focusField) && sendAction.getCursorPosition() == null) {
			throw (new SendActionException(MessageFormat.format("Cursor field was not found{0} in screen {1}", focusField,
					screenEntity.getClass())));
		}
		return sendAction;
	}
}
