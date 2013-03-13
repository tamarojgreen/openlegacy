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

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleBooleanFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleFieldWthValuesTypeDefinition;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Action;
import org.openlegacy.designtime.terminal.generators.support.DefaultScreenPojoCodeModel.Field;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.support.SimpleDisplayItem;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenNavigationDefinition;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.services.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringConstants;
import org.openlegacy.utils.StringUtil;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
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
		String treatNullAsEmpty = getAnnotationValue(annotationExpr, AnnotationConstants.TREAT_EMPTY_AS_NULL);
		FieldTypeDefinition booleanFieldDefiniton = new SimpleBooleanFieldTypeDefinition(trueValue, falseValue,
				StringConstants.TRUE.equals(treatNullAsEmpty));
		field.setFieldTypeDefinition(booleanFieldDefiniton);
	}

	public static void loadFieldValues(AnnotationExpr annotationExpr, Field field) {
		String sourceScreenClassValue = getAnnotationValue(annotationExpr, AnnotationConstants.SOURCE_SCREEN_ENTITY);
		String sourceEntityClassName = StringUtil.toClassName(sourceScreenClassValue);
		String collectAll = getAnnotationValue(annotationExpr, AnnotationConstants.COLLECT_ALL);

		SimpleFieldWthValuesTypeDefinition fieldDefinition = new SimpleFieldWthValuesTypeDefinition();
		fieldDefinition.setSourceEntityClassName(sourceEntityClassName);
		if (StringConstants.TRUE.equals(collectAll)) {
			fieldDefinition.setCollectAllRecords(true);
		}
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
		// @author Ivan Bort refs assembla #112
		String fieldTypeName = getAnnotationValue(annotationExpr, AnnotationConstants.FIELD_TYPE);
		String rectangleValue = getAnnotationValue(annotationExpr, AnnotationConstants.RECTANGLE);
		String passwordValue = getAnnotationValue(annotationExpr, AnnotationConstants.PASSWORD);
		String sampleValue = getAnnotationValue(annotationExpr, AnnotationConstants.SAMPLE_VALUE);
		String rowsOffset = getAnnotationValue(annotationExpr, AnnotationConstants.ROWS_OFFSET);

		field.setSampleValue(StringUtil.isEmpty(sampleValue) ? "" : StringUtil.stripQuotes(sampleValue));
		field.setFieldTypeName(StringUtil.toClassName(fieldTypeName));

		if (StringConstants.TRUE.equals(rectangleValue)) {
			field.setRectangle(true);
		}
		if (StringConstants.TRUE.equals(passwordValue)) {
			field.setPassword(true);
		}

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
		if (rowsOffset != null) {
			field.setRowsOffset(Integer.valueOf(rowsOffset));
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
				String targetEntityName = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.TARGET_ENTITY);

				Action action = new Action(actionAlias, actionClassName, displayName);
				action.setActionValue(actionValue == null ? "" : actionValue);
				action.setTargetEntityName(StringUtil.toClassName(targetEntityName));
				actions.add(action);
			}
		}
		return actions;
	}

	public static NavigationDefinition populateNavigation(AnnotationExpr annotationExpr) {
		SimpleScreenNavigationDefinition navigationDefinition = new SimpleScreenNavigationDefinition();

		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> navigationAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();

			for (MemberValuePair memberValuePair : navigationAttributes) {
				String attributeValue = memberValuePair.getValue().toString();
				if (memberValuePair.getName().equals(AnnotationConstants.ACCESSED_FROM)) {
					navigationDefinition.setAccessedFromEntityName(StringUtil.stripQuotes(StringUtil.toClassName(attributeValue)));
				}
				if (memberValuePair.getName().equals(AnnotationConstants.REQUIRES_PARAMETERS)) {
					navigationDefinition.setRequiresParameters(Boolean.valueOf(attributeValue));
				}
				if (memberValuePair.getName().equals(AnnotationConstants.TERMINAL_ACTION)) {
					navigationDefinition.setTerminalActionName(StringUtil.toClassName(attributeValue));
				}
				if (memberValuePair.getName().equals(AnnotationConstants.ADDITIONAL_KEY)) {
					navigationDefinition.setAdditionalKey(AdditionalKey.valueOf(attributeValue.split("\\.")[1]));
				}
				if (memberValuePair.getName().equals(AnnotationConstants.EXIT_ACTION)) {
					navigationDefinition.setExitActionName(StringUtil.toClassName(attributeValue));
				}
				if (memberValuePair.getName().equals(AnnotationConstants.EXIT_ADDITIONAL_KEY)) {
					navigationDefinition.setExitAdditionalKey(AdditionalKey.valueOf(attributeValue.split("\\.")[1]));
				}
				if (memberValuePair.getName().equals(AnnotationConstants.ASSIGNED_FIELDS)) {
					navigationDefinition.setAssignedFields(populateAssignedFields(memberValuePair.getValue()));
				}
			}
		}
		return navigationDefinition;
	}

	private static List<FieldAssignDefinition> populateAssignedFields(Expression expression) {
		List<FieldAssignDefinition> list = new ArrayList<FieldAssignDefinition>();
		if (expression instanceof ArrayInitializerExpr) {
			List<Expression> values = ((ArrayInitializerExpr)expression).getValues();
			for (Expression expr : values) {
				if (expr instanceof NormalAnnotationExpr
						&& (((NormalAnnotationExpr)expr).getName().getName().equals(AssignedField.class.getSimpleName()))) {
					NormalAnnotationExpr annotationExpr = (NormalAnnotationExpr)expr;
					List<MemberValuePair> pairs = annotationExpr.getPairs();
					String name = null;
					String value = null;
					for (MemberValuePair pair : pairs) {
						String attrValue = pair.getValue().toString();
						if (pair.getName().equals(AnnotationConstants.FIELD)) {
							name = StringUtil.stripQuotes(attrValue);
						}
						if (pair.getName().equals(AnnotationConstants.VALUE)) {
							value = StringUtil.stripQuotes(attrValue);
						}
					}
					list.add(new SimpleFieldAssignDefinition(name, value));
				}
			}
		}
		return list;
	}

	public static ScreenIdentification populateScreenIdentification(AnnotationExpr annotationExpr) {
		SimpleScreenIdentification identification = new SimpleScreenIdentification();
		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> identifiersAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();
			if (identifiersAttributes == null) {
				return identification;
			}
			for (MemberValuePair memberValuePair : identifiersAttributes) {
				if (memberValuePair.getName().equals(AnnotationConstants.IDENTIFIERS)) {
					List<ScreenIdentifier> list = populateScreenIdentifiers(memberValuePair.getValue());
					for (ScreenIdentifier screenIdentifier : list) {
						identification.addIdentifier(screenIdentifier);
					}
				}
			}
		}
		return identification;
	}

	private static List<ScreenIdentifier> populateScreenIdentifiers(Expression expression) {
		List<ScreenIdentifier> list = new ArrayList<ScreenIdentifier>();
		if (expression instanceof ArrayInitializerExpr) {
			List<Expression> values = ((ArrayInitializerExpr)expression).getValues();
			for (Expression expr : values) {
				if (expr instanceof NormalAnnotationExpr
						&& (((NormalAnnotationExpr)expr).getName().getName().equals(Identifier.class.getSimpleName()))) {
					NormalAnnotationExpr annotationExpr = (NormalAnnotationExpr)expr;
					List<MemberValuePair> pairs = annotationExpr.getPairs();
					int row = 0;
					int column = 0;
					String value = null;
					for (MemberValuePair pair : pairs) {
						String attrValue = pair.getValue().toString();
						if (pair.getName().equals(AnnotationConstants.ROW)) {
							row = Integer.parseInt(attrValue);
						}
						if (pair.getName().equals(AnnotationConstants.COLUMN)) {
							column = Integer.parseInt(attrValue);
						}
						if (pair.getName().equals(AnnotationConstants.VALUE)) {
							value = StringUtil.stripQuotes(attrValue);
						}
					}
					list.add(new SimpleScreenIdentifier(new SimpleTerminalPosition(row, column), value));
				}
			}
		}
		return list;
	}

	/**
	 * @param mainType
	 * @param fieldDeclaration
	 * @param field
	 */
	public static void loadEnumField(ClassOrInterfaceDeclaration mainType, FieldDeclaration fieldDeclaration, Field field) {
		String fieldType = fieldDeclaration.getType().toString();

		List<BodyDeclaration> members = mainType.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof EnumDeclaration) {
				EnumDeclaration enumDeclaration = (EnumDeclaration)bodyDeclaration;
				if (enumDeclaration.getName().equals(fieldType)) {
					SimpleEnumFieldTypeDefinition enumDefinition = new SimpleEnumFieldTypeDefinition();

					List<EnumConstantDeclaration> entries = enumDeclaration.getEntries();
					for (EnumConstantDeclaration entry : entries) {
						String value = "";
						String displayName = "";

						List<Expression> args = entry.getArgs();
						if (args.size() == 1) {
							value = StringUtil.stripQuotes(args.get(0).toString());
						} else if (args.size() >= 2) {
							value = StringUtil.stripQuotes(args.get(0).toString());
							displayName = StringUtil.stripQuotes(args.get(1).toString());
						}
						enumDefinition.getEnums().put(entry.getName(), new SimpleDisplayItem(value, displayName));
					}
					field.setFieldTypeDefinition(enumDefinition);
					break;
				}
			}
		}
	}
}
