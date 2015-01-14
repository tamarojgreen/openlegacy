package org.openlegacy.terminal.mock_session.mock2;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.actions.TerminalActions.F4;
import org.openlegacy.terminal.support.AbstractScreenEntity;

import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(column = 15, row = 3, value = "** Window **") })
@ScreenNavigation(accessedFrom = FormMock.class, terminalAction = F4.class)
public class WindowMock extends AbstractScreenEntity {

	private List<WindowMockRecord> windowMockRecords;

	public List<WindowMockRecord> getWindowMockRecords() {
		return windowMockRecords;
	}

	@ScreenTable(endRow = 7, startRow = 6)
	public static class WindowMockRecord {

		@ScreenColumn(endColumn = 2, startColumn = 2, editable = true, selectionField = true)
		private String selection;

		@ScreenColumn(endColumn = 13, startColumn = 5)
		private String optionA;

		public String getSelection() {
			return selection;
		}

		public void setSelection(String selection) {
			this.selection = selection;
		}

		public String getOptionA() {
			return optionA;
		}
	}
}
