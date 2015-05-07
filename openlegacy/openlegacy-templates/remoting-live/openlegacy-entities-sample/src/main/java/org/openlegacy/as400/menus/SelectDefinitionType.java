package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;

@ScreenEntity()
@ScreenIdentifiers(identifiers = {
		@Identifier(row = 1, column = 2, value = "                           Select Definition Type                              "),
		@Identifier(row = 5, column = 2, value = "  Definition type . . . . . . ."),
		@Identifier(row = 9, column = 2, value = "  Data dictionary . . . . . . .") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit"),
		@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"),
		@Action(action = TerminalActions.F10.class, displayName = "Work with database files", alias = "workWithDatabaseFiles"),
		@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"),
		@Action(action = TerminalActions.F10.class, additionalKey = AdditionalKey.SHIFT, displayName = "Work with data dictionaries", alias = "workWithDataDictionaries") })
@ScreenNavigation(accessedFrom = InteractiveDataDefinitionUtility.class, assignedFields = { @AssignedField(field = "menuSelection", value = "1") })
public class SelectDefinitionType implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 5, column = 36, endColumn = 36, labelColumn = 2, editable = true, displayName = "Definition type")
	private DefinitionType definitionType;

	@ScreenField(row = 9, column = 36, endColumn = 45, labelColumn = 2, editable = true, displayName = "Data dictionary")
	private String dataDictionary;

	public enum DefinitionType implements Serializable {
		Field,
		RecordFormat,
		File
	}
}
