package apps.position.screens;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenTable;

import java.util.List;

@ScreenEntity(validateKeys = false)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 23, value = "DISPLAY THE LOADALL SUBFILE") })
public class PositionDrillDownMenu {

	@ScreenField(column = 45, endColumn = 49, row = 23, editable = false)
	String rrn;

	private List<DisplayTheLoadallSubfileRecord> displayTheLoadallSubfileRecords;

	@ScreenTable(startRow = 8, endRow = 12)
	public static class DisplayTheLoadallSubfileRecord {

		@ScreenColumn(startColumn = 12, endColumn = 14, key = true, displayName = "Column1", sampleValue = "123")
		private Integer column1;
		@ScreenColumn(startColumn = 28, endColumn = 39, mainDisplayField = true, displayName = "Column2", sampleValue = "123456789abc")
		private String column2;
		@ScreenColumn(startColumn = 49, endColumn = 51, displayName = "Column3", sampleValue = "123")
		private Integer column3;

	}

}
