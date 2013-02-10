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

import static org.openlegacy.designtime.utils.JavaParserUtil.getAnnotationValue;

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleFieldWthValuesTypeDefinition;
import org.openlegacy.definitions.support.SimpleBooleanFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Action;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.utils.StringConstants;
import org.openlegacy.utils.StringUtil;

import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.util.ArrayList;
import java.util.List;

public class ScreenAnnotationsParserUtils {

	public static void loadDateField(AnnotationExpr annotationExpr, Field field) {
		Integer yearColumn = Integer.valueOf(getAnnotationValue(annotationExpr, AnnotationConstants.YEAR_COLUMN));
		Integer monthColumn = Integer.valueOf(getAnnotationValue(annotationExpr, AnnotationConstants.MONTH_COLUMN));
		Integer dayColumn = Integer.valueOf(getAnnotationValue(annotationExpr, AnnotationConstants.DAY_COLUMN));
		FieldTypeDefinition dateFieldDefiniton = new SimpleDateFieldTypeDefinition(dayColumn, monthColumn, yearColumn);
		field.setFieldTypeDefinition(dateFieldDefiniton);
	}

	public static void loadBooleanField(AnnotationExpr annotationExpr, Field field) {
		String trueValue = getAnnotationValue(annotationExpr, AnnotationConstants.TRUE_VALUE);
		String falseValue = getAnnotationValue(annotationExpr, AnnotationConstants.FALSE_VALUE);
		FieldTypeDefinition booleanFieldDefiniton = new SimpleBooleanFieldTypeDefinition(trueValue, falseValue, false);
		field.setFieldTypeDefinition(booleanFieldDefiniton);
	}

	public static void loadFieldValues(AnnotationExpr annotationExpr, Field field) {
		String sourceScreenClassValue = getAnnotationValue(annotationExpr, AnnotationConstants.SOURCE_SCREEN_ENTITY);
		String sourceEntityClassName = StringUtil.toClassName(sourceScreenClassValue);

		SimpleFieldWthValuesTypeDefinition fieldDefinition = new SimpleFieldWthValuesTypeDefinition();
		fieldDefinition.setSourceEntityClassName(sourceEntityClassName);
		field.setFieldTypeDefinition(fieldDefinition);
		// used for aspectj code generation
		field.setHasValues(true);
	}

	public static void loadScreenFieldOrColumnAnnotation(AnnotationExpr annotationExpr, Field field) {
		String editableValue = getAnnotationValue(annotationExpr, AnnotationConstants.EDITABLE);
		String rowValue = getAnnotationValue(annotationExpr, AnnotationConstants.ROW);
		String columnValue = getAnnotationValue(annotationExpr, AnnotationConstants.COLUMN);
		String displayNameValue = getAnnotationValue(annotationExpr, AnnotationConstants.DISPLAY_NAME);
		String startColumnValue = getAnnotationValue(annotationExpr, AnnotationConstants.START_COLUMN);
		String endColumnValue = getAnnotationValue(annotationExpr, AnnotationConstants.END_COLUMN);
		String labelColumnValue = getAnnotationValue(annotationExpr, AnnotationConstants.LABEL_COLUMN);
		String helpTextValue = getAnnotationValue(annotationExpr, AnnotationConstants.HELP_TEXT);
		String selectionFieldValue = getAnnotationValue(annotationExpr, AnnotationConstants.SELECTION_FIELD);
		String keyValue = getAnnotationValue(annotationExpr, AnnotationConstants.KEY);
		String mainDisplayFieldValue = getAnnotationValue(annotationExpr, AnnotationConstants.MAIN_DISPLAY_FIELD);

		if (AnnotationConstants.TRUE.equals(editableValue)) {
			field.setEditable(true);
		}
		if (rowValue != null) {
			field.setRow(Integer.valueOf(rowValue));
		}
		if (columnValue != null) {
			field.setColumn(Integer.valueOf(columnValue));
		}
		String displayName = !StringUtil.isEmpty(displayNameValue) ? displayNameValue : StringUtil.toDisplayName(field.getName());
		field.setDisplayName(displayName);

		if (startColumnValue != null) {
			field.setColumn(Integer.valueOf(startColumnValue));
		}
		if (endColumnValue != null) {
			field.setEndColumn(Integer.valueOf(endColumnValue));
		}
		if (labelColumnValue != null) {
			field.setLabelColumn(Integer.valueOf(labelColumnValue));
		}
		if (helpTextValue != null) {
			field.setHelpText(StringUtil.stripQuotes(helpTextValue));
		}

		if (StringConstants.TRUE.equals(selectionFieldValue)) {
			field.setSelectionField(true);
		}
		if (StringConstants.TRUE.equals(keyValue)) {
			field.setKey(true);
		}
		if (StringConstants.TRUE.equals(mainDisplayFieldValue)) {
			field.setMainDisplayField(true);
		}
	}

	public static List<Action> populateScreenActions(AnnotationExpr annotationExpr) {
		List<Action> actions = new ArrayList<Action>();

		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> actionAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();
			MemberValuePair actionsKeyValue = actionAttributes.get(0);
			ArrayInitializerExpr actionsPairs = (ArrayInitializerExpr)actionsKeyValue.getValue();
			List<Expression> actionsAnnotations = actionsPairs.getValues();
			for (Expression expression : actionsAnnotations) {
				NormalAnnotationExpr singleAction = (NormalAnnotationExpr)expression;
				String actionClassName = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.ACTION);
				String displayName = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.DISPLAY_NAME);
				String actionAlias = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.ALIAS);
				// used by @TableAction only
				String actionValue = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.ACTION_VALUE);

				Action action = new Action(actionAlias, actionClassName, displayName);
				action.setActionValue(actionValue);
				actions.add(action);
			}
		}
		return actions;
	}

}
