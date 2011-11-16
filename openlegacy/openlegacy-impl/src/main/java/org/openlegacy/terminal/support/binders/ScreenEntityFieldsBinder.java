package org.openlegacy.terminal.support.binders;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.exceptions.SendActionException;
import org.openlegacy.terminal.providers.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Data binder implementation for building a screenEntity from a given terminal screen, and build fields to send from a given
 * screenEntity
 * 
 */
@Component
public class ScreenEntityFieldsBinder implements ScreenEntityBinder {

	@Inject
	private FieldMappingsDefinitionProvider fieldMappingsProvider;

	@Inject
	private ScreenBinderLogic screenBinderLogic;

	public void populateEntity(Object screenEntity, TerminalScreen terminalScreen) throws HostEntityNotFoundException,
			ScreenEntityNotAccessibleException {

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		fieldAccessor.setTerminalScreen(terminalScreen);

		Collection<FieldMappingDefinition> fieldMappingDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity.getClass());
		screenBinderLogic.populatedFields(fieldAccessor, terminalScreen, fieldMappingDefinitions);
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalScreen terminalScreen, Object entity) {

		if (entity == null) {
			return;
		}

		Assert.isTrue(entity instanceof ScreenEntity, "screen entity must implement ScreenEntity interface");

		ScreenEntity screenEntity = (ScreenEntity)entity;

		Collection<FieldMappingDefinition> fieldMappingsDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(
				terminalScreen, screenEntity.getClass());

		if (fieldMappingsDefinitions == null) {
			return;
		}

		screenBinderLogic.populateSendAction(sendAction, terminalScreen, screenEntity, fieldMappingsDefinitions);

		if (!StringUtils.isEmpty(screenEntity.getFocusField()) && sendAction.getCursorPosition() == null) {
			throw (new SendActionException(MessageFormat.format("Cursor field {0} was not found in screen {1}",
					screenEntity.getFocusField(), ProxyUtil.getOriginalClass(screenEntity.getClass()))));
		}
	}

}
