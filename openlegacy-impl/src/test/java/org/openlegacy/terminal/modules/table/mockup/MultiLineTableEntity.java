package org.openlegacy.terminal.modules.table.mockup;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 26, value = "Items") })
public class MultiLineTableEntity implements org.openlegacy.terminal.ScreenEntity {

	private List<ItemsListRow> itemListRows;

	public List<ItemsListRow> getItemListRows() {
		return itemListRows;
	}

	@ScreenTable(startRow = 8, endRow = 19, rowGaps = 2)
	public static class ItemsListRow {

		@ScreenColumn(startColumn = 4, endColumn = 4, editable = true, selectionField = true)
		private String action;

		@ScreenColumn(startColumn = 11, endColumn = 28, key = true)
		private Integer itemNumber;

		@ScreenColumn(startColumn = 30, endColumn = 43)
		private String alphaSearch;

		@Autowired
		@ScreenColumn(startColumn = 14, endColumn = 45, rowsOffset = 1)
		private String itemDescription;

		public String getAction() {
			return action;
		}

		public Integer getItemNumber() {
			return itemNumber;
		}

		public String getAlphaSearch() {
			return alphaSearch;
		}

		public String getItemDescription() {
			return itemDescription;
		}

	}

	public String getFocusField() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFocusField(String focusField) {
		// TODO Auto-generated method stub

	}

}
