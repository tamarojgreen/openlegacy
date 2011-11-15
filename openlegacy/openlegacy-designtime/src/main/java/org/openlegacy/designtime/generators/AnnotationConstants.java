package org.openlegacy.designtime.generators;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.annotations.screen.ScreenTable;

public class AnnotationConstants {

	public static final String SCREEN_ENTITY_ANNOTATION = ScreenEntity.class.getSimpleName();
	public static final String SCREEN_PART_ANNOTATION = ScreenPart.class.getSimpleName();
	public static final String SCREEN_TABLE_ANNOTATION = ScreenTable.class.getSimpleName();
	public static final String SCREEN_COLUMN_ANNOTATION = ScreenColumn.class.getSimpleName();
	public static final String TRUE = "true";
	public static final String FIELD_SUFFIX = "Field";
	public static final String FIELD_MAPPING_ANNOTATION = FieldMapping.class.getSimpleName();

	public static final String SUPPORT_TERMINAL_DATA = "supportTerminalData";
	public static final String EDITABLE = "editable";
}
