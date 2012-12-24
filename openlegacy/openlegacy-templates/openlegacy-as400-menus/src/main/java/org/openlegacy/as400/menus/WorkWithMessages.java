package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity()
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 32, value = "Work with Messages"),
		@Identifier(row = 8, column = 2, value = "Opt"), @Identifier(row = 8, column = 8, value = "Message") })
@ScreenActions(actions = {
		@Action(action = TerminalActions.F6.class, displayName = "Display system operator messages", alias = "displaySystemOperatorMessages"),
		@Action(action = TerminalActions.F4.class, additionalKey = AdditionalKey.SHIFT, displayName = "Remove messages not needing a reply", alias = "removeMessagesNotNeedingAReply"),
		@Action(action = TerminalActions.F5.class, additionalKey = AdditionalKey.SHIFT, displayName = "Top", alias = "top") })
@ScreenNavigation(accessedFrom = UserTasks.class, assignedFields = { @AssignedField(field = "menuSelection", value = "2") })
public class WorkWithMessages {

	@ScreenField(row = 3, column = 18, endColumn = 37, labelColumn = 2, displayName = "Messages for", sampleValue = "RMR20924")
	private String messagesFor;

	private List<WorkWithMessagesRecord> workWithMessagesRecords;

	@ScreenTable(endRow = 13, startRow = 9)
	public static class WorkWithMessagesRecord {

		@ScreenColumn(endColumn = 3, startColumn = 3, selectionField = true)
		private String opt;

		@ScreenColumn(endColumn = 60, startColumn = 8)
		private String message;
	}
}
