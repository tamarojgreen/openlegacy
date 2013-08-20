/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.terminal.generators.support;

import org.openlegacy.annotations.screen.PartPosition;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenBooleanField;
import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenDateField;
import org.openlegacy.annotations.screen.ScreenDescriptionField;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenEntitySuperClass;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.annotations.screen.ScreenPart;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableActions;

public class ScreenAnnotationConstants {

	public static final String SCREEN_ENTITY_ANNOTATION = ScreenEntity.class.getSimpleName();
	public static final String SCREEN_ENTITY_SUPER_CLASS_ANNOTATION = ScreenEntitySuperClass.class.getSimpleName();
	public static final String SCREEN_PART_ANNOTATION = ScreenPart.class.getSimpleName();
	public static final String SCREEN_TABLE_ANNOTATION = ScreenTable.class.getSimpleName();
	public static final String SCREEN_COLUMN_ANNOTATION = ScreenColumn.class.getSimpleName();
	public static final String SCREEN_FIELD_ANNOTATION = ScreenField.class.getSimpleName();
	public static final String SCREEN_ACTIONS_ANNOTATION = ScreenActions.class.getSimpleName();
	public static final String SCREEN_FIELD_VALUES_ANNOTATION = ScreenFieldValues.class.getSimpleName();
	public static final String SCREEN_BOOLEAN_FIELD_ANNOTATION = ScreenBooleanField.class.getSimpleName();
	public static final String SCREEN_DATE_FIELD_ANNOTATION = ScreenDateField.class.getSimpleName();
	public static final String SCREEN_DESCRIPTION_FIELD_ANNOTATION = ScreenDescriptionField.class.getSimpleName();
	public static final String SCREEN_TABLE_ACTIONS_ANNOTATION = ScreenTableActions.class.getSimpleName();
	public static final String SCREEN_NAVIGATION_ANNOTATION = ScreenNavigation.class.getSimpleName();
	public static final String SCREEN_IDENTIFIERS_ANNOTATION = ScreenIdentifiers.class.getSimpleName();
	public static final String PART_POSITION_ANNOTATION = PartPosition.class.getSimpleName();

	public static final String SUPPORT_TERMINAL_DATA = "supportTerminalData";
	public static final String ROW = "row";
	public static final String COLUMN = "column";
	public static final String START_COLUMN = "startColumn";
	public static final String END_COLUMN = "endColumn";
	public static final String START_ROW = "startRow";
	public static final String END_ROW = "endRow";
	public static final String LABEL_COLUMN = "labelColumn";
	public static final String SCREEN_TYPE = "screenType";
	public static final String CHILD = "child";

	// @ScreenEntity
	public static final String WINDOW = "window";
	public static final String ROWS = "rows";
	public static final String COLUMNS = "columns";

	// @ScreenFieldValues
	public static final String SOURCE_SCREEN_ENTITY = "sourceScreenEntity";
	public static final String COLLECT_ALL = "collectAll";

	// @TableAction
	public static final String ACTION_VALUE = "actionValue";
	public static final String DEFAULT_ACTION = "defaultAction";
	public static final String TARGET_ENTITY = "targetEntity";

	// @ScreenColumn
	public static final String SELECTION_FIELD = "selectionField";
	public static final String ROWS_OFFSET = "rowsOffset";

	// @ScreenNavigation
	public static final String ACCESSED_FROM = "accessedFrom";
	public static final String TERMINAL_ACTION = "terminalAction";
	public static final String ADDITIONAL_KEY = "additionalKey";
	public static final String ASSIGNED_FIELDS = "assignedFields";
	public static final String EXIT_ACTION = "exitAction";
	public static final String EXIT_ADDITIONAL_KEY = "exitAdditionalKey";
	public static final String REQUIRES_PARAMETERS = "requiresParameters";

	// @AssignedField
	public static final String FIELD = "field";
	public static final String VALUE = "value";

	// @ScreenField
	public static final String RECTANGLE = "rectangle";
	public static final String ATTRIBUTE = "attribute";
	public static final String WHEN = "when";
	public static final String UNLESS = "unless";

	// @ScreenIdentifiers
	public static final String IDENTIFIERS = "identifiers";

	// @ScreenActions
	public static final String ACTIONS = "actions";

	// @ScreenTable
	public static final String NEXT_SCREEN_ACTION = "nextScreenAction";
	public static final String PREV_SCREEN_ACTION = "previousScreenAction";
	public static final String TABLE_COLLECTOR = "tableCollector";
	public static final String SCROLLABLE = "scrollable";
	public static final String ROW_GAPS = "rowGaps";

	// @PartPosition
	public static final String WIDTH = "width";
	public static final String ACTION = "action";
}
