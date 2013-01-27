package org.openlegacy.terminal.support.binders;

import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;

import javax.inject.Inject;

@Component
public class FieldAttributeBinder implements ScreenEntityBinder {

	@Inject
	private ScreenFieldsDefinitionProvider fieldMappingsProvider;

	public void populateEntity(Object screenEntity, TerminalSnapshot snapshot) {

		Class<? extends Object> class1 = ProxyUtil.getOriginalClass(screenEntity.getClass());

		Collection<ScreenFieldDefinition> fieldDefinitions = fieldMappingsProvider.getFieldsMappingDefinitions(snapshot, class1);

		for (ScreenFieldDefinition fieldDefinition : fieldDefinitions) {
			if (fieldDefinition.getAttribute() == FieldAttributeType.Value) {
				continue;
			}

			TerminalPosition position = fieldDefinition.getPosition();
			TerminalField terminalField = snapshot.getField(position);
			if (terminalField == null) {
				continue;
			}

			String fieldName = fieldDefinition.getName();
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
			if (fieldDefinition.getAttribute() == FieldAttributeType.Editable) {

				fieldAccessor.setFieldValue(fieldName, terminalField.isEditable());

			} else if (fieldDefinition.getAttribute() == FieldAttributeType.Color) {
				fieldAccessor.setFieldValue(fieldName, terminalField.getColor());
			}
		}
	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot snapshot, Object entity) {

	}
}
