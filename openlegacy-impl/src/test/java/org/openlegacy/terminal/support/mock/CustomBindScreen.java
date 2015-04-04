package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenBinder;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.support.mock.CustomBindScreen.CustomBinder;

import java.util.Collections;
import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Custom bind title") })
@ScreenBinder(binders = { CustomBinder.class })
public class CustomBindScreen implements org.openlegacy.terminal.ScreenEntity {

	String fieldAandB;

	public String getFieldAandB() {
		return fieldAandB;
	}

	public void setFieldAandB(String fieldAandB) {
		this.fieldAandB = fieldAandB;
	}

	@Override
	public String getFocusField() {
		return null;
	}

	@Override
	public void setFocusField(String focusField) {}

	@Override
	@SuppressWarnings("unchecked")
	public List<TerminalActionDefinition> getActions() {
		return Collections.EMPTY_LIST;
	}

	public static class CustomBinder implements ScreenEntityBinder {

		private static final long serialVersionUID = 1L;

		@Override
		public void populateEntity(Object entity, TerminalSnapshot snapshot) {
			CustomBindScreen customBindScreen = (CustomBindScreen)entity;
			customBindScreen.setFieldAandB(snapshot.getField(3, 15).getValue() + snapshot.getField(4, 15).getValue());
		}

		@Override
		public void populateAction(TerminalSendAction sendAction, TerminalSnapshot snapshot, Object entity) {
			CustomBindScreen customBindScreen = (CustomBindScreen)entity;
			String fielAandB = customBindScreen.getFieldAandB();
			TerminalField fieldA = snapshot.getField(3, 15);
			fieldA.setValue(fielAandB.substring(0, 2));
			TerminalField fieldB = snapshot.getField(4, 15);
			fieldB.setValue(fielAandB.substring(2));
			sendAction.getFields().add(fieldA);
			sendAction.getFields().add(fieldB);
		}
	}
}
