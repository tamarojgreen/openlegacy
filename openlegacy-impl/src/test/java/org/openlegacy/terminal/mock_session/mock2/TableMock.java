package org.openlegacy.terminal.mock_session.mock2;

import java.util.List;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.terminal.support.AbstractScreenEntity;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(column = 15, row = 1, value = "** Table **") })
public class TableMock extends AbstractScreenEntity {

	private List<TableMockRecord> tableMockRecords;
	
	public List<TableMockRecord> getTableMockRecords() {
		return tableMockRecords;
	}
	
	@ScreenTable(endRow = 4, startRow = 3)
	public static class TableMockRecord{
		@ScreenColumn(endColumn = 2, startColumn = 2,editable=true,selectionField=true)
		private String selection;
		
		@ScreenColumn(endColumn = 10, startColumn = 5,key=true)
		private String columnA;
		
		public String getSelection() {
			return selection;
		}
		
		public void setSelection(String selection) {
			this.selection = selection;
		}
		
		public String getColumnA() {
			return columnA;
		}
	}
}
