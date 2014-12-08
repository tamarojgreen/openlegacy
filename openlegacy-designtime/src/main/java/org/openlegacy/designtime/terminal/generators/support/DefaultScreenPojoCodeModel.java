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
package org.openlegacy.designtime.terminal.generators.support;

import static org.openlegacy.designtime.utils.JavaParserUtil.findAnnotationAttribute;
import static org.openlegacy.designtime.utils.JavaParserUtil.findAnnotationStringAttributes;

import org.openlegacy.FieldType.General;
import org.openlegacy.annotations.screen.Action.ActionType;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.generators.AnnotationsParserUtils;
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.table.ScreenTableCollector;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.StringConstants;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.TypesUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 */
public class DefaultScreenPojoCodeModel implements ScreenPojoCodeModel {

	private static class PartPostition {

		private TerminalPosition partPosition;
		private int width;

		public PartPostition(int row, int column, int width) {
			this.partPosition = new SimpleTerminalPosition(row, column);
			this.width = width;
		}

		public TerminalPosition getPartPosition() {
			return partPosition;
		}

		public int getWidth() {
			return width;
		}
	}

	public static class Action {

		private String alias;
		private String actionName;
		private String displayName;
		private String actionValue;
		private String targetEntityName;
		private AdditionalKey additionalKey;
		private int row;
		private int column;
		private int length;
		private String when;
		private String focusField;
		private ActionType type;
		private int sleep;
		private boolean global;
		private String keyboardKeyName;

		public Action(String alias, String actionName, String displayName, AdditionalKey additionalKey, int row, int column,
				int length, String when, String focusField, ActionType type, int sleep, boolean global) {
			this.alias = alias;
			this.actionName = actionName;
			this.displayName = displayName;
			this.additionalKey = additionalKey;
			this.row = row;
			this.column = column;
			this.length = length;
			this.when = when;
			this.focusField = focusField;
			this.type = type;
			this.targetEntityName = ScreenEntity.NONE.class.getSimpleName();
			this.sleep = sleep;
			this.global = global;
			this.keyboardKeyName = TerminalActions.NONE.class.getSimpleName();
		}

		public String getActionName() {
			return actionName;
		}

		public String getActionValue() {
			return actionValue;
		}

		public String getAlias() {
			return alias;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getTargetEntityName() {
			return targetEntityName;
		}

		public void setActionValue(String actionValue) {
			this.actionValue = actionValue;
		}

		public void setTargetEntityName(String targetEntityName) {
			this.targetEntityName = targetEntityName;
		}

		public AdditionalKey getAdditionalKey() {
			return additionalKey;
		}

		public void setAdditionalKey(AdditionalKey additionalKey) {
			this.additionalKey = additionalKey;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public String getWhen() {
			return when;
		}

		public void setWhen(String when) {
			this.when = when;
		}

		public String getFocusField() {
			return focusField;
		}

		public void setFocusField(String focusField) {
			this.focusField = focusField;
		}

		public ActionType getType() {
			return type;
		}

		public void setType(ActionType type) {
			this.type = type;
		}

		public int getSleep() {
			return sleep;
		}

		public void setSleep(int sleep) {
			this.sleep = sleep;
		}

		public boolean isGlobal() {
			return global;
		}

		public void setGlobal(boolean global) {
			this.global = global;
		}

		public String getKeyboardKeyName() {
			return keyboardKeyName;
		}

		public void setKeyboardKeyName(String keyboardKeyName) {
			this.keyboardKeyName = keyboardKeyName;
		}

	}

	/**
	 * Model for properties of @ScreenField or @ScreenColumn
	 */
	public static class Field {

		private String name;
		private String displayName;
		private boolean hasGetter;
		private boolean hasSetter;
		private boolean hasGetterField;
		private boolean staticOrFinal;
		private boolean hasValues;
		private String type;
		private boolean editable;
		private boolean primitiveType;
		private Integer row;
		private Integer column;
		private Integer endColumn;
		private Integer labelColumn;
		private String helpText;
		private FieldTypeDefinition fieldTypeDefiniton;
		private boolean selectionField;
		private boolean key;
		private boolean mainDisplayField;
		// @author Ivan Bort refs assembla #112
		private String fieldTypeName;
		private boolean rectangle;
		private boolean password;
		private String sampleValue;
		private String defaultValue;
		private int rowsOffset;
		private boolean hasDescription;
		// @author Ivan Bort refs assembla #235
		private Integer endRow;
		private boolean rightToLeft;
		private String attributeName;
		private String when;
		private String unless;
		private Integer descriptionRow;
		private Integer descriptionColumn;
		private Integer descriptionEndColumn;
		// @author Ivan Bort refs assembla #483
		private int keyIndex;
		private boolean internal;
		private boolean global;
		private String nullValue;
		private int colSpan;
		private int sortIndex;
		private String targetEntityClassName;

		// @author Ivan Bort, refs assembla #595
		private boolean tableKey;
		private boolean forceUpdate;

		private String expression;
		private boolean enableLookup;

		public Field(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public String getAttributeName() {
			return attributeName;
		}

		public Integer getColumn() {
			return column;
		}

		public String getDisplayName() {
			return displayName;
		}

		public Integer getEndColumn() {
			return endColumn;
		}

		public Integer getEndRow() {
			return endRow;
		}

		public FieldTypeDefinition getFieldTypeDefiniton() {
			return fieldTypeDefiniton;
		}

		public String getFieldTypeName() {
			return fieldTypeName;
		}

		public String getHelpText() {
			return helpText;
		}

		public Integer getLabelColumn() {
			return labelColumn;
		}

		public String getName() {
			return name;
		}

		public Integer getRow() {
			return row;
		}

		public int getRowsOffset() {
			return rowsOffset;
		}

		public String getSampleValue() {
			return sampleValue;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public String getType() {
			return type;
		}

		public String getUnless() {
			return unless;
		}

		public String getWhen() {
			return when;
		}

		public boolean isChildScreenEntityField() {
			return row == null && column == null;
		}

		public boolean isEditable() {
			return editable;
		}

		public boolean isHasDescription() {
			return hasDescription;
		}

		public boolean isHasGetter() {
			return hasGetter;
		}

		public boolean isHasGetterField() {
			return hasGetterField;
		}

		public boolean isHasSetter() {
			return hasSetter;
		}

		public boolean isStaticOrFinal() {
			return staticOrFinal;
		}

		public boolean isHasValues() {
			return hasValues;
		}

		public boolean isKey() {
			return key;
		}

		public boolean isMainDisplayField() {
			return mainDisplayField;
		}

		public boolean isPassword() {
			return password;
		}

		public boolean isPrimitiveType() {
			return primitiveType;
		}

		public boolean isRectangle() {
			return rectangle;
		}

		public boolean isRightToLeft() {
			return rightToLeft;
		}

		public boolean isScreenField() {
			// field is either screen field or screen column. column doesn't have row property
			return row != null;
		}

		public boolean isSelectionField() {
			return selectionField;
		}

		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}

		public void setColumn(Integer column) {
			this.column = column;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public void setEndColumn(Integer endColumn) {
			this.endColumn = endColumn;
		}

		public void setEndRow(Integer endRow) {
			this.endRow = endRow;
		}

		public void setFieldTypeDefinition(FieldTypeDefinition fieldTypeDefiniton) {
			this.fieldTypeDefiniton = fieldTypeDefiniton;
		}

		public void setFieldTypeName(String fieldTypeName) {
			this.fieldTypeName = fieldTypeName;
		}

		public void setHasDescription(boolean hasDescription) {
			this.hasDescription = hasDescription;
		}

		public void setHasGetter(boolean hasGetter) {
			this.hasGetter = hasGetter;
		}

		public void setHasGetterField(boolean hasGetterField) {
			this.hasGetterField = hasGetterField;
		}

		public void setHasSetter(boolean hasSetter) {
			this.hasSetter = hasSetter;
		}

		public void setStaticOrFinal(boolean staticOrFinal) {
			this.staticOrFinal = staticOrFinal;
		}

		public void setHasValues(boolean hasValues) {
			this.hasValues = hasValues;
		}

		public void setHelpText(String helpText) {
			this.helpText = helpText;
		}

		public void setKey(boolean key) {
			this.key = key;
		}

		public void setLabelColumn(Integer labelColumn) {
			this.labelColumn = labelColumn;
		}

		public void setMainDisplayField(boolean mainDisplayField) {
			this.mainDisplayField = mainDisplayField;
		}

		public void setPassword(boolean password) {
			this.password = password;
		}

		public void setPrimitiveType(boolean primitiveType) {
			this.primitiveType = primitiveType;
		}

		public void setRectangle(boolean rectangle) {
			this.rectangle = rectangle;
		}

		public void setRightToLeft(boolean rightToLeft) {
			this.rightToLeft = rightToLeft;
		}

		public void setRow(Integer row) {
			this.row = row;
		}

		public void setRowsOffset(int rowsOffset) {
			this.rowsOffset = rowsOffset;
		}

		public void setSampleValue(String sampleValue) {
			this.sampleValue = sampleValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public void setSelectionField(boolean selectionField) {
			this.selectionField = selectionField;
		}

		public void setUnless(String unless) {
			this.unless = unless;
		}

		public void setWhen(String when) {
			this.when = when;
		}

		public Integer getDescriptionRow() {
			return descriptionRow;
		}

		public void setDescriptionRow(Integer descriptionRow) {
			this.descriptionRow = descriptionRow;
		}

		public Integer getDescriptionColumn() {
			return descriptionColumn;
		}

		public void setDescriptionColumn(Integer descriptionColumn) {
			this.descriptionColumn = descriptionColumn;
		}

		public Integer getDescriptionEndColumn() {
			return descriptionEndColumn;
		}

		public void setDescriptionEndColumn(Integer descriptionEndColumn) {
			this.descriptionEndColumn = descriptionEndColumn;
		}

		public int getKeyIndex() {
			return keyIndex;
		}

		public void setKeyIndex(int keyIndex) {
			this.keyIndex = keyIndex;
		}

		public boolean isInternal() {
			return internal;
		}

		public void setInternal(boolean internal) {
			this.internal = internal;
		}

		public boolean isGlobal() {
			return global;
		}

		public void setGlobal(boolean global) {
			this.global = global;
		}

		public String getNullValue() {
			return nullValue;
		}

		public void setNullValue(String nullValue) {
			this.nullValue = nullValue;
		}

		public int getColSpan() {
			return colSpan;
		}

		public void setColSpan(int colSpan) {
			this.colSpan = colSpan;
		}

		public int getSortIndex() {
			return sortIndex;
		}

		public void setSortIndex(int sortIndex) {
			this.sortIndex = sortIndex;
		}

		public String getTargetEntityClassName() {
			return targetEntityClassName;
		}

		public void setTargetEntityClassName(String targetEntityClassName) {
			this.targetEntityClassName = targetEntityClassName;
		}

		public boolean isTableKey() {
			return tableKey;
		}

		public void setTableKey(boolean tableKey) {
			this.tableKey = tableKey;
		}

		public boolean isForceUpdate() {
			return forceUpdate;
		}

		public void setForceUpdate(boolean forceUpdate) {
			this.forceUpdate = forceUpdate;
		}

		public String getExpression() {
			return expression;
		}

		public void setExpression(String expression) {
			this.expression = expression;
		}

		public boolean isEnableLookup() {
			return enableLookup;
		}

		public void setEnableLookup(boolean enableLookup) {
			this.enableLookup = enableLookup;
		}

	}

	private String className;

	private String packageName;
	private ClassOrInterfaceDeclaration mainType;
	private Map<String, Field> fields = new LinkedHashMap<String, Field>();
	private boolean enabled;
	private boolean supportTerminalData;
	private boolean superClass = false;
	private String displayName;

	private String entityName;
	private int startRow;
	private int endRow;
	private List<Action> actions = new ArrayList<Action>();
	private String parentClassName;
	private String typeName;

	private boolean childScreen;
	private NavigationDefinition navigationDefinition;
	private boolean window;
	private ScreenIdentification screenIdentification;
	private String nextScreenActionName;
	private String previousScreenActionName;
	private String tableCollectorName;

	private boolean scrollable;

	private int rowGaps;
	private int screensCount;

	private PartPostition partPostition;

	private SimpleScreenSize screenSize;

	private boolean serviceInOut = false;

	// @ScreenEntity attrubutes
	private boolean validateKeys = true;
	private boolean rightToLeft = false;
	private List<String> roles = new ArrayList<String>();
	private boolean autoMapKeyboardActions = false;

	// @ScreenTable, refs assembla #185
	private String filterExpression = "";

	private ClassOrInterfaceDeclaration entityDeclaration;

	public DefaultScreenPojoCodeModel(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration type, String className,
			String parentClassName, ClassOrInterfaceDeclaration entityDeclaration) {

		this.entityDeclaration = entityDeclaration;
		mainType = type;
		this.parentClassName = parentClassName;

		if (compilationUnit.getPackage() != null) {
			this.packageName = compilationUnit.getPackage().getName().toString();
		}

		this.className = className;
		calculateClassProperties();

		calculateFieldsProperties();

	}

	private static PartPostition populatePartPositionAttributes(AnnotationExpr annotationExpr) {
		PartPostition partPostition = null;
		String rowFromAnnotation = null;
		String columnFromAnnotation = null;
		String widthFromAnnotation = null;
		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;
			rowFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.ROW, normalAnnotationExpr.getPairs());
			columnFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.COLUMN, normalAnnotationExpr.getPairs());
			widthFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.WIDTH, normalAnnotationExpr.getPairs());
		}
		int row = rowFromAnnotation != null ? Integer.valueOf(rowFromAnnotation) : 0;
		int column = columnFromAnnotation != null ? Integer.valueOf(columnFromAnnotation) : 0;
		int width = widthFromAnnotation != null ? Integer.valueOf(widthFromAnnotation) : 80;
		partPostition = new PartPostition(row, column, width);
		return partPostition;
	}

	private void calculateClassProperties() {
		List<AnnotationExpr> annotations = mainType.getAnnotations();
		if (annotations == null) {
			return;
		}
		for (AnnotationExpr annotationExpr : annotations) {
			String annotationName = annotationExpr.getName().getName();
			if (annotationName.equals(ScreenAnnotationConstants.SCREEN_ENTITY_ANNOTATION)
					|| annotationName.equals(ScreenAnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)
					|| annotationName.equals(ScreenAnnotationConstants.SCREEN_PART_ANNOTATION)
					|| annotationName.equals(ScreenAnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
				enabled = true;
				populateEntityAttributes(annotationExpr);
				if (annotationName.equals(ScreenAnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
					superClass = true;
				}
			}
			if (annotationName.equals(ScreenAnnotationConstants.SCREEN_ACTIONS_ANNOTATION)
					|| annotationName.equals(ScreenAnnotationConstants.SCREEN_TABLE_ACTIONS_ANNOTATION)) {
				actions = ScreenAnnotationsParserUtils.populateScreenActions(annotationExpr);
			}
			if (annotationName.equals(ScreenAnnotationConstants.SCREEN_NAVIGATION_ANNOTATION)) {
				navigationDefinition = ScreenAnnotationsParserUtils.populateNavigation(annotationExpr);
			}
			if (annotationName.equals(ScreenAnnotationConstants.SCREEN_IDENTIFIERS_ANNOTATION)) {
				screenIdentification = ScreenAnnotationsParserUtils.populateScreenIdentification(annotationExpr);
			}
			if (annotationName.equals(ScreenAnnotationConstants.PART_POSITION_ANNOTATION)) {
				partPostition = populatePartPositionAttributes(annotationExpr);
			}
			if (annotationName.equals(AnnotationConstants.SERVICE_INPUT)
					|| annotationName.equals(AnnotationConstants.SERVICE_OUTPUT)) {
				serviceInOut = true;
			}
		}
	}

	private void calculateFieldsProperties() {
		List<BodyDeclaration> members = mainType.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof FieldDeclaration) {
				FieldDeclaration fieldDeclaration = (FieldDeclaration)bodyDeclaration;
				List<VariableDeclarator> variables = fieldDeclaration.getVariables();
				if (variables.size() > 0) {
					List<AnnotationExpr> fieldAnnotations = fieldDeclaration.getAnnotations();
					String fieldName = variables.get(0).getId().getName();
					Field field = new Field(fieldName, fieldDeclaration.getType().toString());
					field.setPrimitiveType(TypesUtil.isPrimitive(fieldDeclaration.getType().toString()));
					if (fieldAnnotations != null && fieldAnnotations.size() > 0) {
						for (AnnotationExpr annotationExpr : fieldAnnotations) {
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									ScreenAnnotationConstants.SCREEN_FIELD_ANNOTATION,
									ScreenAnnotationConstants.SCREEN_COLUMN_ANNOTATION)) {
								ScreenAnnotationsParserUtils.loadScreenFieldOrColumnAnnotation(annotationExpr, field);
								// @author Ivan Bort refs #337
								if (!field.isPrimitiveType() && (!field.getType().equals(Date.class.getSimpleName()))) {
									field.setFieldTypeDefinition(AnnotationsParserUtils.loadEnumField(entityDeclaration,
											fieldDeclaration));
								}
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									ScreenAnnotationConstants.SCREEN_FIELD_VALUES_ANNOTATION)) {
								field.setFieldTypeDefinition(ScreenAnnotationsParserUtils.loadFieldValues(annotationExpr));
								field.setHasValues(true);
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									ScreenAnnotationConstants.SCREEN_BOOLEAN_FIELD_ANNOTATION)) {
								field.setFieldTypeDefinition(AnnotationsParserUtils.loadBooleanField(annotationExpr));
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									ScreenAnnotationConstants.SCREEN_DATE_FIELD_ANNOTATION)) {
								field.setFieldTypeDefinition(AnnotationsParserUtils.loadDateField(annotationExpr));
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									ScreenAnnotationConstants.SCREEN_DESCRIPTION_FIELD_ANNOTATION)) {
								field.setHasDescription(true);
								ScreenAnnotationsParserUtils.loadDescriptionField(annotationExpr, field);
							}
						}
					}
					if (Modifier.isStatic(fieldDeclaration.getModifiers()) || Modifier.isFinal(fieldDeclaration.getModifiers())) {
						field.setStaticOrFinal(true);
					}
					fields.put(fieldName, field);
				}
			}
		}

		checkHasGetterAndSetter(members);
	}

	public boolean hasMethodName(String name) {
		if (name == null || name.trim().length() == 0) {
			return false;
		}
		List<BodyDeclaration> members = mainType.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration)bodyDeclaration;
				if (name.equals(methodDeclaration.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private void checkHasGetterAndSetter(List<BodyDeclaration> members) {
		int numberOfGetters = 0;
		int numberOfProperties = 0;
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration)bodyDeclaration;
				String methodName = methodDeclaration.getName();

				String propertyNameIfGetter = PropertyUtil.getPropertyNameIfGetter(methodName);
				if (propertyNameIfGetter != null) {
					Field field = fields.get(propertyNameIfGetter);
					if (field != null) {
						field.setHasGetter(true);
						numberOfGetters++;
					}
				}

				String propertyNameIfSetter = PropertyUtil.getPropertyNameIfSetter(methodName);
				if (propertyNameIfSetter != null) {
					Field field = fields.get(propertyNameIfSetter);
					if (field != null) {
						field.setHasSetter(true);
					}
				}

				String propertyFieldNameIfGetter = PropertyUtil.getPropertyNameIfGetter(methodName);
				if (propertyFieldNameIfGetter != null) {
					if (propertyFieldNameIfGetter.endsWith(AnnotationConstants.FIELD_SUFFIX)) {
						String simplePropertyName = propertyFieldNameIfGetter.substring(0, propertyFieldNameIfGetter.length()
								- AnnotationConstants.FIELD_SUFFIX.length());
						Field field = fields.get(simplePropertyName);
						if (field != null) {
							field.setHasGetterField(true);
						}
					}
				}
			} else {
				if (bodyDeclaration instanceof FieldDeclaration) {
					numberOfProperties++;
				}
			}
		}
	}

	private void populateEntityAttributes(AnnotationExpr annotationExpr) {
		String displayNameFromAnnotation = null;
		String entityNameFromAnnotation = null;
		String typeNameFromAnnotation = null;
		String childFlagFromAnnotation = null;
		String startRowFromTableAnnotation = null;
		String endRowFromTableAnnotation = null;
		String windowFlagFromAnnotation = null;
		String nextScreenActionNameFromAnnotation = null;
		String previousScreenActionNameFromAnnotation = null;
		String tableCollectorNameFromAnnotation = null;
		String scrollableFromAnnotation = null;
		String rowGapsFromAnnotation = null;
		String supportTerminalDataString = null;
		String screenSizeRowsFromAnnotation = null;
		String screenSizeColumnsFromAnnotation = null;
		String validateKeysFlagFromAnnotations = null;
		String rightToLeftFlagFromAnnotation = null;
		List<String> rolesFromAnnotation = null;
		String screensCountFromAnnotation = null;
		String autoMapKeyboardActionsFromAnnotation = null;
		String filterExpressionFromAnnotation = null;

		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;

			List<MemberValuePair> pairs = normalAnnotationExpr.getPairs();
			supportTerminalDataString = findAnnotationAttribute(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA, pairs);
			displayNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.DISPLAY_NAME, pairs);
			entityNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.NAME, pairs);
			typeNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.SCREEN_TYPE, pairs);
			childFlagFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.CHILD, pairs);
			startRowFromTableAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.START_ROW, pairs);
			endRowFromTableAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.END_ROW, pairs);
			windowFlagFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.WINDOW, pairs);

			nextScreenActionNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.NEXT_SCREEN_ACTION, pairs);
			previousScreenActionNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.PREV_SCREEN_ACTION, pairs);
			tableCollectorNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.TABLE_COLLECTOR, pairs);
			scrollableFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.SCROLLABLE, pairs);
			rowGapsFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.ROW_GAPS, pairs);

			screenSizeRowsFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.ROWS, pairs);
			screenSizeColumnsFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.COLUMNS, pairs);
			validateKeysFlagFromAnnotations = findAnnotationAttribute(ScreenAnnotationConstants.VALIDATE_KEYS, pairs);
			rightToLeftFlagFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.RIGHT_TO_LEFT, pairs);
			rolesFromAnnotation = findAnnotationStringAttributes(ScreenAnnotationConstants.ROLES, pairs);

			screensCountFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.SCREENS_COUNT, pairs);

			autoMapKeyboardActionsFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.AUTO_MAP_KEYBOARD_ACTIONS,
					pairs);

			filterExpressionFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.FILTER_EXPRESSION, pairs);
		}
		supportTerminalData = supportTerminalDataString != null && supportTerminalDataString.equals(AnnotationConstants.TRUE);

		displayName = displayNameFromAnnotation != null ? StringUtil.stripQuotes(displayNameFromAnnotation)
				: StringUtil.toDisplayName(getClassName());
		entityName = entityNameFromAnnotation != null ? StringUtil.stripQuotes(entityNameFromAnnotation)
				: StringUtil.toClassName(getClassName());

		typeName = typeNameFromAnnotation != null ? StringUtil.toClassName(typeNameFromAnnotation)
				: General.class.getSimpleName();

		childScreen = childFlagFromAnnotation != null ? Boolean.valueOf(childFlagFromAnnotation) : false;

		if (startRowFromTableAnnotation != null) {
			startRow = Integer.parseInt(startRowFromTableAnnotation);
		}
		if (endRowFromTableAnnotation != null) {
			endRow = Integer.parseInt(endRowFromTableAnnotation);
		}
		window = windowFlagFromAnnotation != null ? Boolean.valueOf(windowFlagFromAnnotation) : false;

		nextScreenActionName = nextScreenActionNameFromAnnotation != null ? StringUtil.toClassName(nextScreenActionNameFromAnnotation)
				: TerminalActions.PAGE_DOWN().getActionName();
		previousScreenActionName = previousScreenActionNameFromAnnotation != null ? StringUtil.toClassName(previousScreenActionNameFromAnnotation)
				: TerminalActions.PAGE_UP().getActionName();
		tableCollectorName = tableCollectorNameFromAnnotation != null ? StringUtil.toClassName(tableCollectorNameFromAnnotation)
				: ScreenTableCollector.class.getSimpleName();
		scrollable = scrollableFromAnnotation != null ? Boolean.valueOf(scrollableFromAnnotation) : true;
		rowGaps = rowGapsFromAnnotation != null ? Integer.valueOf(rowGapsFromAnnotation) : 1;

		if (screenSizeRowsFromAnnotation == null) {
			screenSizeRowsFromAnnotation = String.valueOf(ScreenSize.DEFAULT_ROWS);
		}
		if (screenSizeColumnsFromAnnotation == null) {
			screenSizeColumnsFromAnnotation = String.valueOf(ScreenSize.DEFAULT_COLUMN);
		}
		screenSize = new SimpleScreenSize(Integer.valueOf(screenSizeRowsFromAnnotation),
				Integer.valueOf(screenSizeColumnsFromAnnotation));
		validateKeys = validateKeysFlagFromAnnotations != null ? Boolean.valueOf(validateKeysFlagFromAnnotations) : true;
		rightToLeft = rightToLeftFlagFromAnnotation != null ? Boolean.valueOf(rightToLeftFlagFromAnnotation) : false;
		roles = rolesFromAnnotation != null ? rolesFromAnnotation : new ArrayList<String>();

		screensCount = screensCountFromAnnotation != null ? Integer.valueOf(screensCountFromAnnotation) : 1;
		autoMapKeyboardActions = StringConstants.TRUE.equals(autoMapKeyboardActionsFromAnnotation) ? true : false;
		filterExpression = filterExpressionFromAnnotation != null ? StringUtil.stripQuotes(filterExpressionFromAnnotation) : "";
	}

	@Override
	public List<Action> getActions() {
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getClassName()
	 */
	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public int getEndRow() {
		return endRow;
	}

	@Override
	public String getEntityName() {
		return entityName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getFields()
	 */
	@Override
	public Collection<Field> getFields() {
		return fields.values();
	}

	@Override
	public String getFormattedClassName() {
		return className.replace(".", "");
	}

	@Override
	public NavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	/**
	 * Returns name of next screen action for @ScreenTable
	 */
	@Override
	public String getNextScreenActionName() {
		return nextScreenActionName;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	@Override
	public TerminalPosition getPartPosition() {
		if (partPostition == null) {
			partPostition = new PartPostition(0, 0, 80);
		}
		return partPostition.getPartPosition();
	}

	@Override
	public int getPartWidth() {
		if (partPostition == null) {
			partPostition = new PartPostition(0, 0, 80);
		}
		return partPostition.getWidth();
	}

	/**
	 * Return name of previous screen action for @ScreenTable
	 */
	@Override
	public String getPreviousScreenActionName() {
		return previousScreenActionName;
	}

	@Override
	public int getRowGaps() {
		return rowGaps;
	}

	@Override
	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	@Override
	public int getStartRow() {
		return startRow;
	}

	/**
	 * Returns name of table collector for @ScreenTable
	 */
	@Override
	public String getTableCollectorName() {
		return tableCollectorName;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}

	@Override
	public boolean isChildScreen() {
		return childScreen;
	}

	public void setChildScreen(boolean childScreen) {
		this.childScreen = childScreen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#isRelevant()
	 */
	@Override
	public boolean isRelevant() {
		return enabled;
	}

	@Override
	public boolean isScrollable() {
		return scrollable;
	}

	public boolean isSuperClass() {
		return superClass;
	}

	@Override
	public boolean isSupportTerminalData() {
		return supportTerminalData;
	}

	@Override
	public boolean isWindow() {
		return window;
	}

	@Override
	public SimpleScreenSize getScreenSize() {
		if (screenSize == null) {
			screenSize = new SimpleScreenSize();
		}
		return screenSize;
	}

	@Override
	public boolean isServiceInOut() {
		return serviceInOut;
	}

	@Override
	public boolean isValidateKeys() {
		return validateKeys;
	}

	@Override
	public boolean isRightToLeft() {
		return rightToLeft;
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	@Override
	public int getScreensCount() {
		return screensCount;
	}

	@Override
	public boolean isAutoMapKeyboardActions() {
		return autoMapKeyboardActions;
	}

	@Override
	public String getFilterExpression() {
		return filterExpression;
	}

}
