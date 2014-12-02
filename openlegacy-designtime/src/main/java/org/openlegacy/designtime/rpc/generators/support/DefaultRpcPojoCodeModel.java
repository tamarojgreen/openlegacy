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
package org.openlegacy.designtime.rpc.generators.support;

import static org.openlegacy.designtime.utils.JavaParserUtil.findAnnotationAttribute;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.generators.AnnotationsParserUtils;
import org.openlegacy.designtime.rpc.generators.RpcPojoCodeModel;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.rpc.definitions.RpcNavigationDefinition;
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
public class DefaultRpcPojoCodeModel implements RpcPojoCodeModel {

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
	 * Model for properties of @RpcField
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
		// @author Ivan Bort refs assembla #112
		private String fieldTypeName;
		private boolean password;
		private String sampleValue;
		private boolean hasDescription;
		// @author Ivan Bort refs assembla #235
		private boolean rightToLeft;
		private int length;
		private String runtimeName;
		private int count = 1;
		private Direction direction;
		// @author Ivan Bort, refs assembla #290
		private String originalName;
		private String defaultValue;

		private String expression;

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
			return getDirection() != Direction.OUTPUT;
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

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public boolean isRpcField() {
			return length > 0;
		}

		public String getRuntimeName() {
			return runtimeName;
		}

		public void setRuntimeName(String runtimeName) {
			this.runtimeName = runtimeName;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public Direction getDirection() {
			return direction;
		}

		public void setDirection(Direction direction) {
			this.direction = direction;
		}

		public String getOriginalName() {
			return originalName;
		}

		public void setOriginalName(String originalName) {
			this.originalName = originalName;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getExpression() {
			return expression;
		}

		public void setExpression(String expression) {
			this.expression = expression;
		}

	}

	private String className;

	private String packageName;
	private ClassOrInterfaceDeclaration mainType;
	private Map<String, Field> fields = new LinkedHashMap<String, Field>();
	private boolean enabled;
	private boolean superClass = false;
	private String displayName;

	private String entityName;
	private String parentClassName;
	private String typeName;

	private List<Action> actions = new ArrayList<Action>();

	private Languages language = Languages.UNDEFINED;
	private String runtimeName;

	private boolean serviceInOut = false;

	private RpcNavigationDefinition navigationDefinition;

	private ClassOrInterfaceDeclaration entityDeclaration;

	public DefaultRpcPojoCodeModel(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration type, String className,
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

	private void calculateClassProperties() {
		List<AnnotationExpr> annotations = mainType.getAnnotations();
		if (annotations == null) {
			return;
		}
		for (AnnotationExpr annotationExpr : annotations) {
			String annotationName = annotationExpr.getName().getName();
			if (annotationName.equals(RpcAnnotationConstants.RPC_ENTITY_ANNOTATION)
					|| annotationName.equals(RpcAnnotationConstants.RPC_PART_ANNOTATION)) {
				enabled = true;
				populateEntityAttributes(annotationExpr);
				if (annotationName.equals(RpcAnnotationConstants.RPC_ENTITY_SUPER_CLASS_ANNOTATION)) {
					superClass = true;
				}
			}
			if (annotationName.equals(RpcAnnotationConstants.RPC_ACTIONS_ANNOTATION)) {
				actions = RpcAnnotationsParserUtils.populateRpcActions(annotationExpr);
			}
			if (annotationName.equals(AnnotationConstants.SERVICE_INPUT)
					|| annotationName.equals(AnnotationConstants.SERVICE_OUTPUT)) {
				serviceInOut = true;
			}
			if (annotationName.equals(RpcAnnotationConstants.RPC_NAVIGATION_ANNOTATION)) {
				navigationDefinition = RpcAnnotationsParserUtils.populateNavigation(annotationExpr);
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
									RpcAnnotationConstants.RPC_FIELD_ANNOTATION)) {
								RpcAnnotationsParserUtils.loadRpcFieldAnnotation(annotationExpr, field);
								if (!field.isPrimitiveType() && !field.getType().equals(Date.class.getSimpleName())) {
									field.setFieldTypeDefinition(AnnotationsParserUtils.loadEnumField(entityDeclaration,
											fieldDeclaration));
								}
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									RpcAnnotationConstants.RPC_BOOLEAN_FIELD_ANNOTATION)) {
								field.setFieldTypeDefinition(AnnotationsParserUtils.loadBooleanField(annotationExpr));
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr, RpcAnnotationConstants.RPC_PART_LIST)) {
								field.setCount(Integer.valueOf(JavaParserUtil.getAnnotationValue(annotationExpr,
										RpcAnnotationConstants.COUNT)));
								String runtimeName = JavaParserUtil.getAnnotationValue(annotationExpr,
										RpcAnnotationConstants.RUNTIME_NAME);
								if (runtimeName != null) {
									field.setRuntimeName(runtimeName.length() > 0 ? runtimeName : field.getName());
								}
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									RpcAnnotationConstants.RPC_NUMERIC_ANNOTATION)) {
								field.setFieldTypeDefinition(AnnotationsParserUtils.loadNumericField(annotationExpr));
							}
							if (JavaParserUtil.isOneOfAnnotationsPresent(annotationExpr,
									RpcAnnotationConstants.RPC_DATE_FIELD_ANNOTATION)) {
								field.setFieldTypeDefinition(AnnotationsParserUtils.loadDateField(annotationExpr));
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
		String languageFromAnnotation = null;

		if (annotationExpr instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr)annotationExpr;

			displayNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.DISPLAY_NAME, normalAnnotationExpr.getPairs());
			entityNameFromAnnotation = findAnnotationAttribute(AnnotationConstants.NAME, normalAnnotationExpr.getPairs());
			typeNameFromAnnotation = findAnnotationAttribute(RpcAnnotationConstants.RPC_TYPE, normalAnnotationExpr.getPairs());
			languageFromAnnotation = findAnnotationAttribute(RpcAnnotationConstants.LANGUAGE, normalAnnotationExpr.getPairs());
		}
		displayName = displayNameFromAnnotation != null ? StringUtil.stripQuotes(displayNameFromAnnotation)
				: StringUtil.toDisplayName(getClassName());
		entityName = entityNameFromAnnotation != null ? StringUtil.stripQuotes(entityNameFromAnnotation)
				: StringUtil.toClassName(getClassName());

		typeName = typeNameFromAnnotation != null ? StringUtil.toClassName(typeNameFromAnnotation)
				: org.openlegacy.rpc.RpcEntityType.General.class.getSimpleName();
		if (languageFromAnnotation != null) {
			language = Languages.valueOf(languageFromAnnotation.split("\\.")[1]);
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
	@Override
	public Languages getLanguage() {
		return language;
	}

	@Override
	public String getRuntimeName() {
		return runtimeName;
	}

	@Override
	public boolean isServiceInOut() {
		return serviceInOut;
	}

	@Override
	public RpcNavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

}
