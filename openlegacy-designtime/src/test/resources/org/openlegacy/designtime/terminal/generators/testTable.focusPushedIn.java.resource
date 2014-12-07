package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity
public class TableScreen {

	private List<TableScreenRecord> tableScreenRecords;

	@ScreenTable(startRow = 8, endRow = 19)
	public static class TableScreenRecord{
	
	    private String focusField; 
	    
		@ScreenColumn(startColumn = 14, endColumn = 22)
		private String column1;

		@ScreenColumn(startColumn = 24, endColumn = 60)
		private String column2;
		
        public void focus(String... field) {
            if (field.length > 0) {
                focusField = field[0];
            } else {
                focusField = "column1";
            }
        }
	}
}
