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
import org.openlegacy.designtime.terminal.generators.ScreenPojoCodeModel;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.services.ScreenIdentification;
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
									AnnotationConstants.SCREEN_FIELD_ANNOTATION, AnnotationConstants.SCREEN_COLUMN_ANNOTATION)) {
								ScreenAnnotationsParserUtils.loadScreenFieldOrColumnAnnotation(annotationExpr, field);
								if (!field.isPrimitiveType()) {
									ScreenAnnotationsParserUtils.loadEnumField(mainType, fieldDeclaration, field);
								}
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									AnnotationConstants.SCREEN_FIELD_VALUES_ANNOTATION)) {
								ScreenAnnotationsParserUtils.loadFieldValues(annotationExpr, field);
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									AnnotationConstants.SCREEN_BOOLEAN_FIELD_ANNOTATION)) {
								ScreenAnnotationsParserUtils.loadBooleanField(annotationExpr, field);
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									AnnotationConstants.SCREEN_DATE_FIELD_ANNOTATION)) {
								ScreenAnnotationsParserUtils.loadDateField(annotationExpr, field);
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									AnnotationConstants.SCREEN_DESCRIPTION_FIELD_ANNOTATION)) {
								field.setHasDescription(true);
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

	private void calculateClassProperties() {
		List<AnnotationExpr> annotations = mainType.getAnnotations();
		if (annotations == null) {
			return;
		}
		for (AnnotationExpr annotationExpr : annotations) {
			String annotationName = annotationExpr.getName().getName();
			if (annotationName.equals(AnnotationConstants.SCREEN_ENTITY_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_PART_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
				enabled = true;
				populateEntityAttributes(annotationExpr);
				if (annotationName.equals(AnnotationConstants.SCREEN_ENTITY_SUPER_CLASS_ANNOTATION)) {
					superClass = true;
				}
			}
			if (annotationName.equals(AnnotationConstants.SCREEN_ACTIONS_ANNOTATION)
					|| annotationName.equals(AnnotationConstants.SCREEN_TABLE_ACTIONS_ANNOTATION)) {
				actions = ScreenAnnotationsParserUtils.populateScreenActions(annotationExpr);
			}
			if (annotationName.equals(AnnotationConstants.SCREEN_NAVIGATION_ANNOTATION)) {
				navigationDefinition = ScreenAnnotationsParserUtils.populateNavigation(annotationExpr);
			}
			if (annotationName.equals(AnnotationConstants.SCREEN_IDENTIFIERS_ANNOTATION)) {
				screenIdentification = ScreenAnnotationsParserUtils.populateScreenIdentification(annotationExpr);
			}
			if (annotationName.equals(AnnotationConstants.PART_POSITION_ANNOTATION)) {
				partPostition = populatePartPositionAttributes(annotationExpr);
			}
		}
	}

	private static PartPostition populatePartPositionAttributes(AnnotationExpr annotationExpr) {
		PartPostition partPostition = null;
		String rowFromAnnotation = null;
		String columnFromAnnotation = null;
		String widthFromAnnotation = null;
		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;
			rowFromAnnotation = findAnnotationAttribute(AnnotationConstants.ROW, normalAnnotationExpr.getPairs());
			columnFromAnnotation = findAnnotationAttribute(AnnotationConstants.COLUMN, normalAnnotationExpr.getPairs());
			widthFromAnnotation = findAnnotationAttribute(AnnotationConstants.WIDTH, normalAnnotationExpr.getPairs());
		}
		int row = rowFromAnnotation != null ? Integer.valueOf(rowFromAnnotation) : 0;
		int column = columnFromAnnotation != null ? Integer.valueOf(columnFromAnnotation) : 0;
		int width = widthFromAnnotation != null ? Integer.valueOf(widthFromAnnotation) : 80;
		partPostition = new PartPostition(row, column, width);
		return partPostition;
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

		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;

			supportTerminalDataString = findAnnotationAttribute(AnnotationConstants.SUPPORT_TERMINAL_DATA,
					normalAnnotationExpr.getPairs());
			displayNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.DISPLAY_NAME, normalAnnotationExpr.getPairs());
			entityNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.NAME, normalAnnotationExpr.getPairs());
			typeNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.SCREEN_TYPE, normalAnnotationExpr.getPairs());
			childFlagFromAnnotation = findAnnotationAttribute(AnnotationConstants.CHILD, normalAnnotationExpr.getPairs());
			startRowFromTableAnnotation = findAnnotationAttribute(AnnotationConstants.START_ROW, normalAnnotationExpr.getPairs());
			endRowFromTableAnnotation = findAnnotationAttribute(AnnotationConstants.END_ROW, normalAnnotationExpr.getPairs());
			windowFlagFromAnnotation = findAnnotationAttribute(AnnotationConstants.WINDOW, normalAnnotationExpr.getPairs());

			nextScreenActionNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.NEXT_SCREEN_ACTION,
					normalAnnotationExpr.getPairs());
			previousScreenActionNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.PREV_SCREEN_ACTION,
					normalAnnotationExpr.getPairs());
			tableCollectorNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.TABLE_COLLECTOR,
					normalAnnotationExpr.getPairs());
			scrollableFromAnnotation = findAnnotationAttribute(AnnotationConstants.SCROLLABLE, normalAnnotationExpr.getPairs());
			rowGapsFromAnnotation = findAnnotationAttribute(AnnotationConstants.ROW_GAPS, normalAnnotationExpr.getPairs());
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#isRelevant()
	 */
	public boolean isRelevant() {
		return enabled;
	}

	public boolean isSuperClass() {
		return superClass;
	}

	public boolean isSupportTerminalData() {
		return supportTerminalData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getClassName()
	 */
	public String getClassName() {
		return className;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getFormattedClassName() {
		return className.replace(".", "");
	}

	public String getPackageName() {
		return packageName;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.generators.ScreenEntityCodeModel#getFields()
	 */
	public Collection<Field> getFields() {
		return fields.values();
	}

	public List<Action> getActions() {
		return actions;
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

		public Field(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public boolean isChildScreenEntityField() {
			return row == null && column == null;
		}

		public boolean isScreenField() {
			// field is either screen field or screen column. column doesn't have row property
			return row != null;
		}

		public String getName() {
			return name;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public boolean isHasGetter() {
			return hasGetter;
		}

		public boolean isHasSetter() {
			return hasSetter;
		}

		public void setHasGetter(boolean hasGetter) {
			this.hasGetter = hasGetter;
		}

		public void setHasSetter(boolean hasSetter) {
			this.hasSetter = hasSetter;
		}

		public boolean isHasGetterField() {
			return hasGetterField;
		}

		public void setHasGetterField(boolean hasGetterField) {
			this.hasGetterField = hasGetterField;
		}

		public String getType() {
			return type;
		}

		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public boolean isPrimitiveType() {
			return primitiveType;
		}

		public void setPrimitiveType(boolean primitiveType) {
			this.primitiveType = primitiveType;
		}

		public Integer getRow() {
			return row;
		}

		public void setRow(Integer row) {
			this.row = row;
		}

		public Integer getColumn() {
			return column;
		}

		public void setColumn(Integer column) {
			this.column = column;
		}

		public Integer getEndColumn() {
			return endColumn;
		}

		public void setEndColumn(Integer endColumn) {
			this.endColumn = endColumn;
		}

		public Integer getLabelColumn() {
			return labelColumn;
		}

		public void setLabelColumn(Integer labelColumn) {
			this.labelColumn = labelColumn;
		}

		public boolean isHasValues() {
			return hasValues;
		}

		public void setHasValues(boolean hasValues) {
			this.hasValues = hasValues;
		}

		public FieldTypeDefinition getFieldTypeDefiniton() {
			return fieldTypeDefiniton;
		}

		public void setFieldTypeDefinition(FieldTypeDefinition fieldTypeDefiniton) {
			this.fieldTypeDefiniton = fieldTypeDefiniton;
		}

		public boolean isSelectionField() {
			return selectionField;
		}

		public void setSelectionField(boolean selectionField) {
			this.selectionField = selectionField;
		}

		public boolean isKey() {
			return key;
		}

		public void setKey(boolean key) {
			this.key = key;
		}

		public boolean isMainDisplayField() {
			return mainDisplayField;
		}

		public void setMainDisplayField(boolean mainDisplayField) {
			this.mainDisplayField = mainDisplayField;
		}

		public String getHelpText() {
			return helpText;
		}

		public void setHelpText(String helpText) {
			this.helpText = helpText;
		}

		public String getFieldTypeName() {
			return fieldTypeName;
		}

		public void setFieldTypeName(String fieldTypeName) {
			this.fieldTypeName = fieldTypeName;
		}

		public boolean isRectangle() {
			return rectangle;
		}

		public void setRectangle(boolean rectangle) {
			this.rectangle = rectangle;
		}

		public boolean isPassword() {
			return password;
		}

		public void setPassword(boolean password) {
			this.password = password;
		}

		public String getSampleValue() {
			return sampleValue;
		}

		public void setSampleValue(String sampleValue) {
			this.sampleValue = sampleValue;
		}

		public int getRowsOffset() {
			return rowsOffset;
		}

		public void setRowsOffset(int rowsOffset) {
			this.rowsOffset = rowsOffset;
		}

		public boolean isHasDescription() {
			return hasDescription;
		}

		public void setHasDescription(boolean hasDescription) {
			this.hasDescription = hasDescription;
		}

	}

	public static class Action {

		private String alias;
		private String actionName;
		private String displayName;
		private String actionValue;
		private String targetEntityName;

		public Action(String alias, String actionName, String displayName) {
			this.alias = alias;
			this.actionName = actionName;
			this.displayName = displayName;
		}

		public String getAlias() {
			return alias;
		}

		public String getActionName() {
			return actionName;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getActionValue() {
			return actionValue;
		}

		public void setActionValue(String actionValue) {
			this.actionValue = actionValue;
		}

		public String getTargetEntityName() {
			return targetEntityName;
		}

		public void setTargetEntityName(String targetEntityName) {
			this.targetEntityName = targetEntityName;
		}
	}

	public boolean isChildScreen() {
		return childScreen;
	}

	public NavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	public boolean isWindow() {
		return window;
	}

	public ScreenIdentification getScreenIdentification() {
		return screenIdentification;
	}

	/**
	 * Returns name of next screen action for @ScreenTable
	 */
	public String getNextScreenActionName() {
		return nextScreenActionName;
	}

	/**
	 * Return name of previous screen action for @ScreenTable
	 */
	public String getPreviousScreenActionName() {
		return previousScreenActionName;
	}

	/**
	 * Returns name of table collector for @ScreenTable
	 */
	public String getTableCollectorName() {
		return tableCollectorName;
	}

	public boolean isScrollable() {
		return scrollable;
	}

	public int getRowGaps() {
		return rowGaps;
	}

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
}
