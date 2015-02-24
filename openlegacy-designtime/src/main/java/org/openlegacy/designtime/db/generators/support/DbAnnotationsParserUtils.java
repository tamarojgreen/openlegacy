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

import static org.openlegacy.designtime.utils.JavaParserUtil.getAnnotationValue;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.db.definitions.DbJoinColumnDefinition;
import org.openlegacy.db.definitions.DbManyToOneDefinition;
import org.openlegacy.db.definitions.DbNavigationDefinition;
import org.openlegacy.db.definitions.DbOneToManyDefinition;
import org.openlegacy.db.definitions.DbTableDefinition;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.db.definitions.SimpleDbJoinColumnDefinition;
import org.openlegacy.db.definitions.SimpleDbManyToOneDefinition;
import org.openlegacy.db.definitions.SimpleDbNavigationDefinition;
import org.openlegacy.db.definitions.SimpleDbOneToManyDefinition;
import org.openlegacy.db.definitions.SimpleDbTableDefinition;
import org.openlegacy.db.definitions.SimpleDbTableUniqueConstraintDefinition;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel.Action;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel.ColumnField;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel.Field;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.utils.JavaParserUtil;
import org.openlegacy.utils.StringConstants;
import org.openlegacy.utils.StringUtil;

import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

public class DbAnnotationsParserUtils {

	public static void loadDbFieldAnnotation(AnnotationExpr annotationExpr, Field field) {
		String editableValue = getAnnotationValue(annotationExpr, AnnotationConstants.EDITABLE);
		String displayNameValue = getAnnotationValue(annotationExpr, AnnotationConstants.DISPLAY_NAME);
		String helpTextValue = getAnnotationValue(annotationExpr, AnnotationConstants.HELP_TEXT);
		String keyValue = getAnnotationValue(annotationExpr, AnnotationConstants.KEY);
		String mainDisplayFieldValue = getAnnotationValue(annotationExpr, AnnotationConstants.MAIN_DISPLAY_FIELD);
		String fieldTypeName = getAnnotationValue(annotationExpr, AnnotationConstants.FIELD_TYPE);
		String passwordValue = getAnnotationValue(annotationExpr, AnnotationConstants.PASSWORD);
		String sampleValue = getAnnotationValue(annotationExpr, AnnotationConstants.SAMPLE_VALUE);
		String rightToLeftValue = getAnnotationValue(annotationExpr, AnnotationConstants.RIGHT_TO_LEFT);

		field.setSampleValue(StringUtil.isEmpty(sampleValue) ? "" : StringUtil.stripQuotes(sampleValue));
		field.setFieldTypeName(StringUtil.toClassName(fieldTypeName));

		if (StringConstants.TRUE.equals(passwordValue)) {
			field.setPassword(true);
		}

		if (AnnotationConstants.TRUE.equals(editableValue)) {
			field.setEditable(true);
		}
		String displayName = displayNameValue != null ? displayNameValue : StringUtil.toDisplayName(field.getName());
		field.setDisplayName(displayName);

		if (helpTextValue != null) {
			field.setHelpText(StringUtil.stripQuotes(helpTextValue));
		}

		if (StringConstants.TRUE.equals(keyValue)) {
			field.setKey(true);
		}
		if (StringConstants.TRUE.equals(mainDisplayFieldValue)) {
			field.setMainDisplayField(true);
		}
		if (StringConstants.TRUE.equals(rightToLeftValue)) {
			field.setRightToLeft(true);
		}
	}

	public static List<Action> populateDbActions(AnnotationExpr annotationExpr) {
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
				String targetEntity = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.TARGET_ENTITY);
				String global = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.GLOBAL);

				Action action = new Action(actionAlias, actionClassName, displayName, null, (StringUtil.isEmpty(global) ? true
						: StringConstants.TRUE.equals(global)));
				action.setTargetEntityName(StringUtil.toClassName(targetEntity));
				actions.add(action);
			}
		}
		return actions;
	}

	public static DbTableDefinition populateDbTable(AnnotationExpr annotationExpr) {
		DbTableDefinition definition = new SimpleDbTableDefinition();

		String nameValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.NAME);
		String catalogValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.CATALOG);
		String schemaValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.SCHEMA);

		if (!StringUtils.isEmpty(nameValue)) {
			definition.setName(StringUtil.stripQuotes(nameValue));
		}
		if (!StringUtils.isEmpty(catalogValue)) {
			definition.setCatalog(StringUtil.stripQuotes(catalogValue));
		}
		if (!StringUtils.isEmpty(schemaValue)) {
			definition.setSchema(StringUtil.stripQuotes(schemaValue));
		}
		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> pairs = ((NormalAnnotationExpr)annotationExpr).getPairs();
			for (MemberValuePair pair : pairs) {
				if (pair.getName().equals(DbAnnotationConstants.UNIQUE_CONSTRAINTS)) {
					Expression expression = pair.getValue();
					if (expression instanceof ArrayInitializerExpr) {
						// uniqueConstraints={@UniqueConstraint(columnNames={"", ""}, name=""),
						// @UniqueConstraint(columnNames={"",""}, name="")}
						ArrayInitializerExpr array = (ArrayInitializerExpr)expression;
						List<Expression> values = array.getValues();
						for (Expression expr : values) {
							definition.getUniqueConstraints().add(populateUniqueConstraintDefinition((NormalAnnotationExpr)expr));
						}
					} else if (expression instanceof NormalAnnotationExpr) {
						// uniqueConstraints=@UniqueConstraint(columnNames="name")
						NormalAnnotationExpr annotation = (NormalAnnotationExpr)expression;
						definition.getUniqueConstraints().add(populateUniqueConstraintDefinition(annotation));
					}
					continue;
				}
			}
		}
		return definition;
	}

	private static UniqueConstraintDefinition populateUniqueConstraintDefinition(NormalAnnotationExpr annotation) {
		SimpleDbTableUniqueConstraintDefinition definition = new SimpleDbTableUniqueConstraintDefinition();

		List<MemberValuePair> pairs = annotation.getPairs();
		for (MemberValuePair pair : pairs) {
			if (pair.getName().equals(DbAnnotationConstants.COLUMN_NAMES)) {
				Expression value = pair.getValue();
				if (value instanceof ArrayInitializerExpr) {
					ArrayInitializerExpr expr = (ArrayInitializerExpr)value;
					List<Expression> values = expr.getValues();
					for (Expression expression : values) {
						definition.getColumnNames().add(StringUtil.stripQuotes(expression.toString()));
					}
				} else {
					definition.getColumnNames().add(StringUtil.stripQuotes(value.toString()));
				}
			} else if (pair.getName().equals(DbAnnotationConstants.NAME)) {
				definition.setName(StringUtil.stripQuotes(pair.getValue().toString()));
			}
		}

		return definition;
	}

	public static void loadJpaColumnAnnotation(AnnotationExpr annotationExpr, ColumnField columnField) {
		String nameValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.NAME);
		String uniqueValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.UNIQUE);
		String nullableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.NULLABLE);
		String insertableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.INSERTABLE);
		String updatableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.UPDATABLE);
		String columnDefinitionValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.COLUMN_DEFINITION);
		String tableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.TABLE);
		String lengthValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.LENGTH);
		String precisionValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.PRECISION);
		String scaleValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.SCALE);

		if (nameValue != null) {
			columnField.setName(nameValue);
		}
		if (StringConstants.TRUE.equals(uniqueValue)) {
			columnField.setUnique(true);
		}
		if (StringConstants.FALSE.equals(nullableValue)) {
			columnField.setNullable(false);
		}
		if (StringConstants.FALSE.equals(insertableValue)) {
			columnField.setInsertable(false);
		}
		if (StringConstants.FALSE.equals(updatableValue)) {
			columnField.setUpdatable(false);
		}
		if (columnDefinitionValue != null) {
			columnField.setColumnDefinition(columnDefinitionValue);
		}
		if (tableValue != null) {
			columnField.setTable(tableValue);
		}
		if (lengthValue != null) {
			columnField.setLength(Integer.valueOf(lengthValue));
		}
		if (precisionValue != null) {
			columnField.setPrecision(Integer.valueOf(precisionValue));
		}
		if (scaleValue != null) {
			columnField.setScale(Integer.valueOf(scaleValue));
		}
	}

	public static void loadDbColumnAnnotation(AnnotationExpr annotationExpr, ColumnField columnField) {
		String displayNameValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.DISPLAY_NAME);
		String passwordValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.PASSWORD);
		String sampleValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.SAMPLE_VALUE);
		String defaultValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.DEFAULT_VALUE);
		String helpTextValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.HELP_TEXT);
		String rightToLeftValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.RIGHT_TO_LEFT);
		String internalValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.INTERNAL);
		String mainDisplayFieldValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.MAIN_DISPLAY_FIELD);

		if (displayNameValue != null) {
			columnField.setDisplayName(displayNameValue);
		}
		if (StringConstants.TRUE.equals(passwordValue)) {
			columnField.setPassword(true);
		}
		if (sampleValue != null) {
			columnField.setSampleValue(sampleValue);
		}
		if (defaultValue != null) {
			columnField.setDefaultValue(defaultValue);
		}
		if (helpTextValue != null) {
			columnField.setHelpText(helpTextValue);
		}
		if (StringConstants.TRUE.equals(rightToLeftValue)) {
			columnField.setRightToLeft(true);
		}
		if (StringConstants.TRUE.equals(internalValue)) {
			columnField.setInternal(true);
		}
		if (StringConstants.TRUE.equals(mainDisplayFieldValue)) {
			columnField.setMainDisplayField(true);
		}
	}

	public static DbOneToManyDefinition loadDbOneToManyDefinition(AnnotationExpr annotationExpr, String currentClassName,
			String typeArgs, File packageDir) {
		SimpleDbOneToManyDefinition definition = null;
		String cascadeValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.CASCADE);
		String fetchValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.FETCH);
		String mappedByValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.MAPPED_BY);
		String orphanRemovalValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.ORPHAN_REMOVAL);
		String targetEntityValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.TARGET_ENTITY);

		if (!StringUtils.isEmpty(cascadeValue) || !StringUtils.isEmpty(fetchValue) || !StringUtils.isEmpty(mappedByValue)
				|| !StringUtils.isEmpty(orphanRemovalValue) || !StringUtils.isEmpty(targetEntityValue)) {
			definition = new SimpleDbOneToManyDefinition();
		} else {
			return null;
		}
		if (!StringUtils.isEmpty(cascadeValue)) {
			definition.setCascadeTypeNames(parseCascadeValue(cascadeValue).toArray(new String[] {}));
		}
		if (!StringUtils.isEmpty(fetchValue)) {
			String[] split = fetchValue.split("\\.");
			definition.setFetchTypeName(split[split.length - 1].trim());
		}
		if (!StringUtils.isEmpty(mappedByValue)) {
			definition.setMappedBy(StringUtil.stripQuotes(mappedByValue));
		}
		if (StringConstants.TRUE.equals(orphanRemovalValue)) {
			definition.setOrphanRemoval(true);
		}
		if (!StringUtils.isEmpty(targetEntityValue)) {
			definition.setTargetEntityClassName(StringUtil.toClassName(targetEntityValue));
			if (typeArgs == null) {
				definition.setTargetEntityDefinition(DbCodeBasedDefinitionUtils.getEntityDefinition(
						StringUtil.toClassName(targetEntityValue), packageDir));
			}
		}

		if (typeArgs != null) {
			String[] strings = typeArgs.split(",");
			if (!strings[strings.length - 1].equals(currentClassName)) {
				definition.setTargetEntityDefinition(DbCodeBasedDefinitionUtils.getEntityDefinition(strings[strings.length - 1],
						packageDir));
			}
		}
		return definition;
	}

	private static List<String> parseCascadeValue(String cascadeValue) {
		List<String> list = new ArrayList<String>();
		if (cascadeValue.startsWith("{")) {
			String value = cascadeValue.replaceAll("[{}]", "");
			String[] split = value.split(",");
			for (String splitValue : split) {
				String[] strings = splitValue.trim().split("\\.");
				list.add(strings[strings.length - 1]);
			}
		} else {
			String[] split = cascadeValue.split("\\.");
			list.add(split[split.length - 1]);
		}
		return list;
	}

	public static DbNavigationDefinition populateNavigation(AnnotationExpr annotationExpr) {
		SimpleDbNavigationDefinition navigationDefinition = new SimpleDbNavigationDefinition();

		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> navigationAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();
			for (MemberValuePair memberValuePair : navigationAttributes) {
				String attributeValue = memberValuePair.getValue().toString();
				if (memberValuePair.getName().equals(DbAnnotationConstants.CATEGORY)) {
					navigationDefinition.setCategory(StringUtil.stripQuotes(attributeValue));
				}
			}
		}
		return navigationDefinition;
	}

	public static DbManyToOneDefinition loadDbManyToOneDefinition(AnnotationExpr annotationExpr) {
		SimpleDbManyToOneDefinition definition = null;
		String targetEntityValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.TARGET_ENTITY);
		String cascadeValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.CASCADE);
		String fetchValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.FETCH);
		String optionalValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.OPTIONAL);

		definition = new SimpleDbManyToOneDefinition();
		if (StringUtils.isEmpty(targetEntityValue) && StringUtils.isEmpty(cascadeValue) && StringUtils.isEmpty(fetchValue)
				&& StringUtils.isEmpty(optionalValue)) {
			return definition;
		}
		if (!StringUtils.isEmpty(targetEntityValue)) {
			definition.setTargetEntityClassName(StringUtil.toClassName(targetEntityValue));
		}
		if (!StringUtils.isEmpty(cascadeValue)) {
			List<String> list = parseCascadeValue(cascadeValue);
			CascadeType[] cascadeTypes = new CascadeType[list.size()];
			for (int i = 0; i < list.size(); i++) {
				cascadeTypes[i] = CascadeType.valueOf(list.get(i));
			}
			definition.setCascade(cascadeTypes);
		}
		if (!StringUtils.isEmpty(fetchValue)) {
			String[] split = fetchValue.split("\\.");
			definition.setFetch(FetchType.valueOf(split[split.length - 1].trim()));
		}
		if (StringConstants.FALSE.equals(optionalValue)) {
			definition.setOptional(false);
		}
		return definition;
	}

	public static DbJoinColumnDefinition loadDbJoinColumnDefinition(AnnotationExpr annotationExpr) {
		SimpleDbJoinColumnDefinition definition = null;
		String nameValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.NAME);
		String referencedColumnNameValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.REFERENCED_COLUMN_NAME);
		String uniqueValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.UNIQUE);
		String nullableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.NULLABLE);
		String insertableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.INSERTABLE);
		String updatableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.UPDATABLE);
		String columnDefinitionValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.COLUMN_DEFINITION);
		String tableValue = getAnnotationValue(annotationExpr, DbAnnotationConstants.TABLE);

		if (!StringUtils.isEmpty(nameValue) || !StringUtils.isEmpty(referencedColumnNameValue)
				|| !StringUtils.isEmpty(uniqueValue) || !StringUtils.isEmpty(nullableValue)
				|| !StringUtils.isEmpty(insertableValue) || !StringUtils.isEmpty(updatableValue)
				|| !StringUtils.isEmpty(columnDefinitionValue) || !StringUtils.isEmpty(tableValue)) {
			definition = new SimpleDbJoinColumnDefinition();
		} else {
			return null;
		}
		if (!StringUtils.isEmpty(nameValue)) {
			definition.setName(nameValue);
		}
		if (!StringUtils.isEmpty(referencedColumnNameValue)) {
			definition.setReferencedColumnName(referencedColumnNameValue);
		}
		if (StringConstants.TRUE.equals(uniqueValue)) {
			definition.setUnique(true);
		}
		if (StringConstants.FALSE.equals(nullableValue)) {
			definition.setNullable(false);
		}
		if (StringConstants.FALSE.equals(insertableValue)) {
			definition.setInsertable(false);
		}
		if (StringConstants.FALSE.equals(updatableValue)) {
			definition.setUpdatable(false);
		}
		if (!StringUtils.isEmpty(columnDefinitionValue)) {
			definition.setColumnDefinition(columnDefinitionValue);
		}
		if (!StringUtils.isEmpty(tableValue)) {
			definition.setTable(tableValue);
		}
		return definition;
	}
}
