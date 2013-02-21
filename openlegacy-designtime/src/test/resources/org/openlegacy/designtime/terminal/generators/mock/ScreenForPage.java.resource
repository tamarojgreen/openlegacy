package org.openlegacy.designtime.terminal.generators.mock;

import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.terminal.actions.TerminalActions;

import java.util.List;

@ScreenEntity
@ScreenActions(actions = { @Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"),
		@Action(action = TerminalActions.F3.class, displayName = "Exit", alias = "exit") })
@ScreenNavigation(accessedFrom = ScreenForPageWithKey.class, exitAction = TerminalActions.F12.class)
public class ScreenForPage implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 22, endColumn = 31, editable = true, helpText="help text for some field")
	private String fldCol12;

	@ScreenField(row = 7, column = 22, endColumn = 31, editable = true)
	private String fld2Col12;

	@ScreenField(row = 8, column = 22, endColumn = 31, editable = true)
	private String fld3Col12;

	@ScreenField(row = 10, column = 33, endColumn = 43)
	@ScreenFieldValues(sourceScreenEntity = LookupWindow.class)
	private String fldRow10;

	@ScreenField(row = 10, column = 53)
	private String fld2Row10;

	@ScreenField(row = 24, column = 20)
	private String orphanFld;

	private ScreenForPagePart screenForPagePart;

	private List<ScreenForPageRow> screenForPageRow;

	public String getFldCol12() {
		return fldCol12;
	}

	public String getFld2Col12() {
		return fld2Col12;
	}

	public String getFld3Col12() {
		return fld3Col12;
	}

	public String getFldRow10() {
		return fldRow10;
	}

	public String getFld2Row10() {
		return fld2Row10;
	}

	public String getOrphanFld() {
		return orphanFld;
	}

	public ScreenForPagePart getScreenForPagePart() {
		return screenForPagePart;
	}

	public List<ScreenForPageRow> getScreenForPageRow() {
		return screenForPageRow;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	@ScreenPart
	public static class ScreenForPagePart {

		@ScreenField(row = 20, column = 13, endColumn = 43)
		private String fldRow20;

		@ScreenField(row = 20, column = 33)
		private String fld2Row20;

		public String getFldRow20() {
			return fldRow20;
		}

		public String getFld2Row20() {
			return fld2Row20;
		}
	}

	@ScreenTable(startRow = 15, endRow = 20)
	@ScreenTableActions(actions = { @TableAction(actionValue = "1", displayName = "View"),
			@TableAction(actionValue = "2", displayName = "Revise") })
	public static class ScreenForPageRow {

		@ScreenColumn(startColumn = 5, endColumn = 6, editable = true, selectionField = true)
		private String action;

		@ScreenColumn(startColumn = 10, endColumn = 20, helpText="help text for some column")
		private String column1;

		@ScreenColumn(startColumn = 25, endColumn = 30)
		private String column2;

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getColumn1() {
			return column1;
		}

		public String getColumn2() {
			return column2;
		}
	}
}
