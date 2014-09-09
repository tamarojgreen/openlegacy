/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.generators;

import org.openlegacy.annotations.ServiceInput;
import org.openlegacy.annotations.ServiceOutput;

public class AnnotationConstants {

	public static final String TRUE = "true";
	public static final String FIELD_SUFFIX = "Field";

	public static final String EDITABLE = "editable";
	public static final String DISPLAY_NAME = "displayName";
	public static final String NAME = "name";
	public static final String ALIAS = "alias";
	public static final String HELP_TEXT = "helpText";
	public static final String ROW = "row";
	public static final String COLUMN = "column";

	public static final String PROVIDER = "provider";

	public static final String ACTIONS = "actions";
	public static final String ACTION = "action";
	public static final String GLOBAL = "global";
	public static final String KEYBOARD_KEY = "keyboardKey";

	// @ScreenBooleanField / @RpcBooleanField
	public static final String TRUE_VALUE = "trueValue";
	public static final String FALSE_VALUE = "falseValue";
	public static final String TREAT_EMPTY_AS_NULL = "treatEmptyAsNull";

	// @ScreenDateField
	public static final String YEAR_COLUMN = "yearColumn";
	public static final String MONTH_COLUMN = "monthColumn";
	public static final String DAY_COLUMN = "dayColumn";
	public static final String PATTERN = "pattern";

	// @TableAction
	public static final String ACTION_VALUE = "actionValue";
	public static final String DEFAULT_ACTION = "defaultAction";
	public static final String TARGET_ENTITY = "targetEntity";

	public static final String KEY = "key";
	public static final String MAIN_DISPLAY_FIELD = "mainDisplayField";

	// @ScreenField
	public static final String PASSWORD = "password";
	public static final String FIELD_TYPE = "fieldType";
	public static final String SAMPLE_VALUE = "sampleValue";
	public static final String DEFAULT_VALUE = "defaultValue";
	public static final String RIGHT_TO_LEFT = "rightToLeft";
	public static final String ATTRIBUTE = "attribute";
	public static final String LENGTH = "length";
	public static final String WHEN = "when";
	public static final Object SERVICE_INPUT = ServiceInput.class.getSimpleName();
	public static final Object SERVICE_OUTPUT = ServiceOutput.class.getSimpleName();

	// @ScreenEntity
	public static final String ROLE = "role";

}
