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
package org.openlegacy.designtime.db.generators.support;

import static org.openlegacy.designtime.utils.JavaParserUtil.findAnnotationAttribute;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.annotations.db.DbEntity;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.db.definitions.DbJoinColumnDefinition;
import org.openlegacy.db.definitions.DbManyToOneDefinition;
import org.openlegacy.db.definitions.DbNavigationDefinition;
import org.openlegacy.db.definitions.DbOneToManyDefinition;
import org.openlegacy.db.definitions.DbTableDefinition;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.designtime.db.generators.DbPojoCodeModel;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.generators.AnnotationsParserUtils;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.utils.PropertyUtil;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.TypesUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

/**
 * 
 * 
 */
public class DefaultDbPojoCodeModel implements DbPojoCodeModel {

	public static class Action {

		private String alias;
		private String actionName;
		private String displayName;
		private String targetEntityName;
		private String path;
		private boolean global;

		public Action(String alias, String actionName, String displayName, String path, boolean global) {
			this.alias = alias;
			this.actionName = actionName;
			this.displayName = displayName;
			this.path = path;
			this.global = global;
		}

		public String getActionName() {
			return actionName;
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

		public void setTargetEntityName(String targetEntityName) {
			this.targetEntityName = targetEntityName;
		}

		public String getPath() {
			return path;
		}

		public boolean isGlobal() {
			return global;
		}
	}

	/**
	 * Model for properties of @DbField
	 */
	public static class Field {

		private String name;
		private String displayName;
		private boolean hasGetter;
		private boolean hasSetter;
		private boolean hasGetterField;
		private boolean staticOrFinal;
		private String type;
		private Boolean editable;
		private boolean primitiveType;
		private String helpText;
		private FieldTypeDefinition fieldTypeDefiniton;
		private boolean key;
		private boolean mainDisplayField;
		private String fieldTypeName;
		private boolean password;
		private String sampleValue;
		private boolean hasDescription;
		private boolean rightToLeft;
		private String defaultValue;

		public Field(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public String getDisplayName() {
			return displayName;
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

		public String getName() {
			return name;
		}

		public String getSampleValue() {
			return sampleValue;
		}

		public String getType() {
			return type;
		}

		public boolean isEditable() {
			if (editable != null) {
				return editable;
			}
			return false;
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

		public boolean isRightToLeft() {
			return rightToLeft;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
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

		public void setHelpText(String helpText) {
			this.helpText = helpText;
		}

		public void setKey(boolean key) {
			this.key = key;
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

		public void setRightToLeft(boolean rightToLeft) {
			this.rightToLeft = rightToLeft;
		}

		public void setSampleValue(String sampleValue) {
			this.sampleValue = sampleValue;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

	}

	/**
	 * Model for properties of @Column
	 * 
	 * @author Ivan Bort
	 */
	public static class ColumnField {

		private String fieldName;
		private String fieldType;
		private String fieldTypeArgs;
		private boolean staticField = false;
		// @Column annotation properties
		private String name = "";
		private boolean unique = false;
		private boolean nullable = true;
		private boolean insertable = true;
		private boolean updatable = true;
		private String columnDefinition = "";
		private String table = "";
		private int length = 255;
		private int precision = 0;
		private int scale = 0;
		// @DbColumn annotation properties
		private String displayName = "";
		private boolean password = false;
		private String sampleValue = "";
		private String defaultValue = "";
		private String helpText = "";
		private boolean rightToLeft = false;
		private boolean internal = false;
		private boolean mainDisplayField = false;

		private boolean key = false;
		private DbOneToManyDefinition oneToManyDefinition;
		private DbManyToOneDefinition manyToOneDefinition;
		private DbJoinColumnDefinition joinColumnDefinition;

		public ColumnField(String fieldName, String fieldType, String fieldTypeArgs) {
			this.fieldName = fieldName;
			this.fieldType = fieldType;
			this.fieldTypeArgs = fieldTypeArgs;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFieldType() {
			return fieldType;
		}

		public void setFieldType(String fieldType) {
			this.fieldType = fieldType;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isUnique() {
			return unique;
		}

		public void setUnique(boolean unique) {
			this.unique = unique;
		}

		public boolean isNullable() {
			return nullable;
		}

		public void setNullable(boolean nullable) {
			this.nullable = nullable;
		}

		public boolean isInsertable() {
			return insertable;
		}

		public void setInsertable(boolean insertable) {
			this.insertable = insertable;
		}

		public boolean isUpdatable() {
			return updatable;
		}

		public void setUpdatable(boolean updatable) {
			this.updatable = updatable;
		}

		public String getColumnDefinition() {
			return columnDefinition;
		}

		public void setColumnDefinition(String columnDefinition) {
			this.columnDefinition = columnDefinition;
		}

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public int getPrecision() {
			return precision;
		}

		public void setPrecision(int precision) {
			this.precision = precision;
		}

		public int getScale() {
			return scale;
		}

		public void setScale(int scale) {
			this.scale = scale;
		}

		public boolean isKey() {
			return key;
		}

		public void setKey(boolean key) {
			this.key = key;
		}

		public DbOneToManyDefinition getOneToManyDefinition() {
			return oneToManyDefinition;
		}

		public void setOneToManyDefinition(DbOneToManyDefinition oneToManyDefinition) {
			this.oneToManyDefinition = oneToManyDefinition;
		}

		public String getFieldTypeArgs() {
			return fieldTypeArgs;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
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

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getHelpText() {
			return helpText;
		}

		public void setHelpText(String helpText) {
			this.helpText = helpText;
		}

		public boolean isRightToLeft() {
			return rightToLeft;
		}

		public void setRightToLeft(boolean rightToLeft) {
			this.rightToLeft = rightToLeft;
		}

		public boolean isInternal() {
			return internal;
		}

		public void setInternal(boolean internal) {
			this.internal = internal;
		}

		public boolean isMainDisplayField() {
			return mainDisplayField;
		}

		public void setMainDisplayField(boolean mainDisplayField) {
			this.mainDisplayField = mainDisplayField;
		}

		public boolean isStaticField() {
			return staticField;
		}

		public void setStaticField(boolean staticField) {
			this.staticField = staticField;
		}

		public DbManyToOneDefinition getManyToOneDefinition() {
			return manyToOneDefinition;
		}

		public void setManyToOneDefinition(DbManyToOneDefinition manyToOneDefinition) {
			this.manyToOneDefinition = manyToOneDefinition;
		}

		public DbJoinColumnDefinition getJoinColumnDefinition() {
			return joinColumnDefinition;
		}

		public void setJoinColumnDefinition(DbJoinColumnDefinition joinColumnDefinition) {
			this.joinColumnDefinition = joinColumnDefinition;
		}

	}

	private String className;

	private String packageName;
	private ClassOrInterfaceDeclaration mainType;
	private Map<String, Field> fields = new LinkedHashMap<String, Field>();
	private boolean enabled;
	private boolean superClass = false;
	private String displayName = "";

	private String entityName;
	private String parentClassName;
	private String typeName;

	private List<Action> actions = new ArrayList<Action>();

	private Languages language = Languages.UNDEFINED;
	private String runtimeName;

	private boolean serviceInOut = false;

	private String name = "";
	private DbTableDefinition tableDefinition = null;
	private Map<String, ColumnField> columnFields = new LinkedHashMap<String, ColumnField>();

	private DbNavigationDefinition navigationDefinition;

	private boolean window = false;
	private boolean child = false;

	public DefaultDbPojoCodeModel(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration type, String className,
			String parentClassName) {

		mainType = type;
		this.parentClassName = parentClassName;

		if (compilationUnit.getPackage() != null) {
			this.packageName = compilationUnit.getPackage().getName().toString();
		}

		this.className = className;
		calculateClassProperties();

		calculateFieldsProperties();

		calculateColumnsProperties();
	}

	private void calculateClassProperties() {
		List<AnnotationExpr> annotations = mainType.getAnnotations();
		if (annotations == null) {
			return;
		}
		for (AnnotationExpr annotationExpr : annotations) {
			String annotationName = annotationExpr.getName().getName();
			if (annotationName.equals(DbAnnotationConstants.DB_JPA_ENTITY_ANNOTATION)
					|| annotationName.equals(DbAnnotationConstants.DB_ENTITY_ANNOTATION)
					|| annotationName.equals(DbAnnotationConstants.DB_ENTITY_SUPER_CLASS_ANNOTATION)) {
				enabled = true;
				populateEntityAttributes(annotationExpr);
				if (annotationName.equals(DbAnnotationConstants.DB_ENTITY_SUPER_CLASS_ANNOTATION)) {
					superClass = true;
				}
			}
			if (annotationName.equals(DbAnnotationConstants.DB_ACTIONS_ANNOTATION)) {
				actions = DbAnnotationsParserUtils.populateDbActions(annotationExpr);
			}
			if (annotationName.equals(AnnotationConstants.SERVICE_INPUT)
					|| annotationName.equals(AnnotationConstants.SERVICE_OUTPUT)) {
				serviceInOut = true;
			}
			if (annotationName.equals(DbAnnotationConstants.DB_TABLE_ANNOTATION)) {
				tableDefinition = DbAnnotationsParserUtils.populateDbTable(annotationExpr);
			}

			if (annotationName.equals(DbAnnotationConstants.DB_NAVIGATION_ANNOTATION)) {
				navigationDefinition = DbAnnotationsParserUtils.populateNavigation(annotationExpr);
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
									DbAnnotationConstants.DB_FIELD_ANNOTATION)) {
								DbAnnotationsParserUtils.loadDbFieldAnnotation(annotationExpr, field);
								if (!field.isPrimitiveType()) {
									field.setFieldTypeDefinition(AnnotationsParserUtils.loadEnumField(mainType, fieldDeclaration));
								}
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr, DbAnnotationConstants.DB_ID_ANNOTATION)) {
								field.setKey(true);
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

	private void calculateColumnsProperties() {
		List<BodyDeclaration> members = mainType.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof FieldDeclaration) {
				FieldDeclaration fieldDeclaration = (FieldDeclaration)bodyDeclaration;
				List<VariableDeclarator> variables = fieldDeclaration.getVariables();
				if (variables.size() > 0) {
					List<AnnotationExpr> fieldAnnotations = fieldDeclaration.getAnnotations();
					String fieldName = variables.get(0).getId().getName();
					String fieldType = null;
					String fieldTypeArgs = null;
					Type type = fieldDeclaration.getType();
					if (type instanceof ReferenceType) {
						ReferenceType referenceType = (ReferenceType)type;
						if (referenceType.getType() instanceof ClassOrInterfaceType) {
							ClassOrInterfaceType type2 = (ClassOrInterfaceType)referenceType.getType();
							fieldType = type2.getName();
							List<Type> typeArgs = type2.getTypeArgs();
							if (typeArgs != null && !typeArgs.isEmpty()) {
								fieldTypeArgs = StringUtils.join(typeArgs, ",");
							}
						} else {
							fieldType = referenceType.toString();
						}
					} else {
						fieldType = type.toString();
					}
					ColumnField columnField = new ColumnField(fieldName, fieldType, fieldTypeArgs);
					if (ModifierSet.hasModifier(fieldDeclaration.getModifiers(), ModifierSet.STATIC)) {
						columnField.setStaticField(true);
					}
					if (fieldAnnotations != null && !fieldAnnotations.isEmpty()) {
						for (AnnotationExpr annotationExpr : fieldAnnotations) {
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									DbAnnotationConstants.DB_JPA_COLUMN_ANNOTATION)) {
								DbAnnotationsParserUtils.loadJpaColumnAnnotation(annotationExpr, columnField);
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									DbAnnotationConstants.DB_COLUMN_ANNOTATION)) {
								DbAnnotationsParserUtils.loadDbColumnAnnotation(annotationExpr, columnField);
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									DbAnnotationConstants.DB_ONE_TO_MANY_ANNOTATION)) {
								columnField.setOneToManyDefinition(DbAnnotationsParserUtils.loadDbOneToManyDefinition(annotationExpr));
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr, DbAnnotationConstants.DB_ID_ANNOTATION)) {
								columnField.setKey(true);
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									DbAnnotationConstants.DB_MANY_TO_ONE_ANNOTATION)) {
								columnField.setManyToOneDefinition(DbAnnotationsParserUtils.loadDbManyToOneDefinition(annotationExpr));
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									DbAnnotationConstants.DB_JOIN_COLUMN_ANNOTATION)) {
								columnField.setJoinColumnDefinition(DbAnnotationsParserUtils.loadDbJoinColumnDefinition(annotationExpr));
							}
						}
					}
					columnFields.put(fieldName, columnField);
				}
			}
		}
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
		String nameFromAnnotation = null;
		String displayNameFromAnnotation = null;
		String windowFromAnnotation = null;
		String childFromAnnotation = null;

		if (annotationExpr instanceof NormalAnnotationExpr
				&& annotationExpr.getName().getName().equals(Entity.class.getSimpleName())) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;

			nameFromAnnotation = findAnnotationAttribute(DbAnnotationConstants.NAME, normalAnnotationExpr.getPairs());

			name = nameFromAnnotation != null ? StringUtil.stripQuotes(nameFromAnnotation) : "";
		}
		if (annotationExpr instanceof NormalAnnotationExpr
				&& annotationExpr.getName().getName().equals(DbEntity.class.getSimpleName())) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;

			displayNameFromAnnotation = findAnnotationAttribute(DbAnnotationConstants.DISPLAY_NAME,
					normalAnnotationExpr.getPairs());
			windowFromAnnotation = findAnnotationAttribute(DbAnnotationConstants.WINDOW, normalAnnotationExpr.getPairs());
			childFromAnnotation = findAnnotationAttribute(DbAnnotationConstants.CHILD, normalAnnotationExpr.getPairs());

			displayName = displayNameFromAnnotation != null ? StringUtil.stripQuotes(displayNameFromAnnotation) : "";
			window = windowFromAnnotation != null ? Boolean.valueOf(windowFromAnnotation) : false;
			child = childFromAnnotation != null ? Boolean.valueOf(childFromAnnotation) : false;
		}
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
	public String getPackageName() {
		return packageName;
	}

	public String getParentClassName() {
		return parentClassName;
	}

	@Override
	public String getTypeName() {
		return typeName;
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

	public boolean isSuperClass() {
		return superClass;
	}

	@Override
	public List<Action> getActions() {
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel#getLanguage()
	 */
	public Languages getLanguage() {
		return language;
	}

	public String getRuntimeName() {
		return runtimeName;
	}

	@Override
	public boolean isServiceInOut() {
		return serviceInOut;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DbTableDefinition getTableDefinition() {
		return tableDefinition;
	}

	@Override
	public Map<String, ColumnField> getColumnFields() {
		return columnFields;
	}

	@Override
	public DbNavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	@Override
	public boolean isWindow() {
		return window;
	}

	@Override
	public boolean isChild() {
		return child;
	}

}
