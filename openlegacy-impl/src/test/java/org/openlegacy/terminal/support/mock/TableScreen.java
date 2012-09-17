package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.modules.table.RecordSelectionEntity;

import java.util.List;

@ScreenEntity(screenType = RecordSelectionEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 40, value = "Table Screen") })
public class TableScreen implements org.openlegacy.terminal.ScreenEntity {

	private List<TableScreenRecord> tableScreenRecords;

	@ScreenTable(startRow = 5, endRow = 7)
	@ScreenTableActions(actions = { @TableAction(actionValue = "1", displayName = "View"),
			@TableAction(actionValue = "2", displayName = "Edit") })
	public static class TableScreenRecord {

		@ScreenColumn(startColumn = 4, endColumn = 5, editable = true, selectionField = true, displayName = "Action")
		private String action_;

		@ScreenColumn(startColumn = 11, endColumn = 19, key = true, displayName = "Key Column", sampleValue = "11")
		private String keyColumn;

		@ScreenColumn(startColumn = 21, endColumn = 29, mainDisplayField = true, displayName = "Val Column", sampleValue = "OneOne")
		private String valColumn;

		public String getAction_() {
			return action_;
		}

		public void setAction_(String action_) {
			this.action_ = action_;
		}

		public String getKeyColumn() {
			return keyColumn;
		}

		public String getValColumn() {
			return valColumn;
		}
	}

	public List<TableScreenRecord> getTableScreenRecords() {
		return tableScreenRecords;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}
}
