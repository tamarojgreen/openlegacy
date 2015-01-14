package org.openlegacy.terminal.mock_session.mock2;

import java.util.List;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.support.AbstractScreenEntity;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(column = 15, row = 1, value = "** Window **") })
public class WindowMock extends AbstractScreenEntity {

	private List<WindowMockRecord> windowMockRecords;
	
	public List<WindowMockRecord> getWindowMockRecords() {
		return windowMockRecords;
	}
	
	@ScreenTable(endRow = 7, startRow = 6)
	public static class WindowMockRecord{
		@ScreenColumn(endColumn = 2, startColumn = 2,editable=true,selectionField=true)
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
