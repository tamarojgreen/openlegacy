package org.openlegacy.designtime.generators.mock;

import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenTable;

import java.util.List;

@ScreenEntity
public class LookupWindow {

	private List<LookupWindowRow> lookupWindowRows;

	public List<LookupWindowRow> getLookupWindowRows() {
		return lookupWindowRows;
	}

	public void setLookupWindowRows(List<LookupWindowRow> lookupWindowRows) {
		this.lookupWindowRows = lookupWindowRows;
	}

	@ScreenTable(startRow = 10, endRow = 13)
	public static class LookupWindowRow {

		@ScreenColumn(startColumn = 8, endColumn = 8, editable = true, selectionField = true)
		private String action;

		@ScreenColumn(startColumn = 14, endColumn = 15, key = true)
		private String fieldValue;

		@ScreenColumn(startColumn = 20, endColumn = 59, mainDisplayField = true)
		private String fieldDescription;

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getFieldValue() {
			return fieldValue;
		}

		public void setFieldValue(String fieldValue) {
			this.fieldValue = fieldValue;
		}

		public String getFieldDescription() {
			return fieldDescription;
		}

		public void setFieldDescription(String fieldDescription) {
			this.fieldDescription = fieldDescription;
		}

	}
}
