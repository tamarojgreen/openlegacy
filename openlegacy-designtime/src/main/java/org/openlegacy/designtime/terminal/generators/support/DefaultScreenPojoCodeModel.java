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

import static org.openlegacy.designtime.utils.JavaParserUtil.findAnnotationAttribute;

import org.openlegacy.FieldType.General;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.generators.AnnotationsParserUtils;
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.utils.JavaParserUtil;
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
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.TypesUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

		public Action(String alias, String actionName, String displayName, AdditionalKey additionalKey) {
			this.alias = alias;
			this.actionName = actionName;
			this.displayName = displayName;
			this.additionalKey = additionalKey;
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

	}

	private String className;

	private String packageName;
	private ClassOrInterfaceDeclaration mainType;
	private Map<String, Field> fields = new TreeMap<String, Field>();
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

	private PartPostition partPostition;

	private SimpleScreenSize screenSize;

	public DefaultScreenPojoCodeModel(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration type, String className,
			String parentClassName) {

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
								if (!field.isPrimitiveType()) {
									field.setFieldTypeDefinition(AnnotationsParserUtils.loadEnumField(mainType, fieldDeclaration));
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
					fields.put(fieldName, field);
				}
			}
		}

		checkHasGetterAndSetter(members);
	}

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
		if (numberOfProperties == numberOfGetters) {
			enabled = false;
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

		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;

			supportTerminalDataString = findAnnotationAttribute(ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA,
					normalAnnotationExpr.getPairs());
			displayNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.DISPLAY_NAME, normalAnnotationExpr.getPairs());
			entityNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.NAME, normalAnnotationExpr.getPairs());
			typeNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.SCREEN_TYPE,
					normalAnnotationExpr.getPairs());
			childFlagFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.CHILD, normalAnnotationExpr.getPairs());
			startRowFromTableAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.START_ROW,
					normalAnnotationExpr.getPairs());
			endRowFromTableAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.END_ROW,
					normalAnnotationExpr.getPairs());
			windowFlagFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.WINDOW, normalAnnotationExpr.getPairs());

			nextScreenActionNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.NEXT_SCREEN_ACTION,
					normalAnnotationExpr.getPairs());
			previousScreenActionNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.PREV_SCREEN_ACTION,
					normalAnnotationExpr.getPairs());
			tableCollectorNameFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.TABLE_COLLECTOR,
					normalAnnotationExpr.getPairs());
			scrollableFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.SCROLLABLE,
					normalAnnotationExpr.getPairs());
			rowGapsFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.ROW_GAPS, normalAnnotationExpr.getPairs());

			screenSizeRowsFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.ROWS,
					normalAnnotationExpr.getPairs());
			screenSizeColumnsFromAnnotation = findAnnotationAttribute(ScreenAnnotationConstants.COLUMNS,
					normalAnnotationExpr.getPairs());
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
				: TerminalActions.PAGEDOWN().getActionName();
		previousScreenActionName = previousScreenActionNameFromAnnotation != null ? StringUtil.toClassName(previousScreenActionNameFromAnnotation)
				: TerminalActions.PAGEUP().getActionName();
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
	}

	public List<Action> getActions() {
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getClassName()
	 */
	public String getClassName() {
		return className;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getEndRow() {
		return endRow;
	}

	public String getEntityName() {
		return entityName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getFields()
	 */
	public Collection<Field> getFields() {
		return fields.values();
	}

	public String getFormattedClassName() {
		return className.replace(".", "");
	}

	public NavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	/**
	 * Returns name of next screen action for @ScreenTable
	 */
	public String getNextScreenActionName() {
		return nextScreenActionName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	public TerminalPosition getPartPosition() {
		if (partPostition == null) {
			partPostition = new PartPostition(0, 0, 80);
		}
		return partPostition.getPartPosition();
	}

	public int getPartWidth() {
		if (partPostition == null) {
			partPostition = new PartPostition(0, 0, 80);
		}
		return partPostition.getWidth();
	}

	/**
	 * Return name of previous screen action for @ScreenTable
	 */
	public String getPreviousScreenActionName() {
		return previousScreenActionName;
	}

	public int getRowGaps() {
		return rowGaps;
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	public int getStartRow() {
		return startRow;
	}

	/**
	 * Returns name of table collector for @ScreenTable
	 */
	public String getTableCollectorName() {
		return tableCollectorName;
	}

	public String getTypeName() {
		return typeName;
	}

	public boolean isChildScreen() {
		return childScreen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#isRelevant()
	 */
	public boolean isRelevant() {
		return enabled;
	}

	public boolean isScrollable() {
		return scrollable;
	}

	public boolean isSuperClass() {
		return superClass;
	}

	public boolean isSupportTerminalData() {
		return supportTerminalData;
	}

	public boolean isWindow() {
		return window;
	}

	public SimpleScreenSize getScreenSize() {
		if (screenSize == null) {
			screenSize = new SimpleScreenSize();
		}
		return screenSize;
	}
}
