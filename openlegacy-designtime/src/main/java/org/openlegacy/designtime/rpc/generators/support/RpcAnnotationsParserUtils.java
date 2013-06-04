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
package org.openlegacy.designtime.rpc.generators.support;

import static org.openlegacy.designtime.utils.JavaParserUtil.getAnnotationValue;

import org.openlegacy.annotations.screen.AssignedField;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Action;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Field;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
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

public class RpcAnnotationsParserUtils {

	public static void loadRpcFieldAnnotation(AnnotationExpr annotationExpr, Field field) {
		String editableValue = getAnnotationValue(annotationExpr, AnnotationConstants.EDITABLE);
		String displayNameValue = getAnnotationValue(annotationExpr, AnnotationConstants.DISPLAY_NAME);
		String helpTextValue = getAnnotationValue(annotationExpr, AnnotationConstants.HELP_TEXT);
		String keyValue = getAnnotationValue(annotationExpr, AnnotationConstants.KEY);
		String mainDisplayFieldValue = getAnnotationValue(annotationExpr, ScreenAnnotationConstants.MAIN_DISPLAY_FIELD);
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

	public static List<Action> populateRpcActions(AnnotationExpr annotationExpr) {
		List<Action> actions = new ArrayList<Action>();

		if (annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> actionAttributes = ((NormalAnnotationExpr)annotationExpr).getPairs();
			MemberValuePair actionsKeyValue = actionAttributes.get(0);
			ArrayInitializerExpr actionsPairs = (ArrayInitializerExpr)actionsKeyValue.getValue();
			List<Expression> actionsAnnotations = actionsPairs.getValues();
			for (Expression expression : actionsAnnotations) {
				NormalAnnotationExpr singleAction = (NormalAnnotationExpr)expression;
				String actionClassName = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.ACTION);
				String displayName = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.DISPLAY_NAME);
				String actionAlias = JavaParserUtil.getAnnotationValue(singleAction, AnnotationConstants.ALIAS);
				String targetEntityName = JavaParserUtil.getAnnotationValue(singleAction, ScreenAnnotationConstants.TARGET_ENTITY);
				String additionalKeyValue = JavaParserUtil.getAnnotationValue(singleAction,
						ScreenAnnotationConstants.ADDITIONAL_KEY);

				AdditionalKey additionalKey = AdditionalKey.NONE;
				if (additionalKeyValue != null) {
					additionalKey = AdditionalKey.valueOf(additionalKeyValue.split("\\.")[1]);
				}
				Action action = new Action(actionAlias, actionClassName, displayName, additionalKey);
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
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.ACCESSED_FROM)) {
					navigationDefinition.setAccessedFromEntityName(StringUtil.stripQuotes(StringUtil.toClassName(attributeValue)));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.REQUIRES_PARAMETERS)) {
					navigationDefinition.setRequiresParameters(Boolean.valueOf(attributeValue));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.TERMINAL_ACTION)) {
					navigationDefinition.setTerminalActionName(StringUtil.toClassName(attributeValue));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.ADDITIONAL_KEY)) {
					navigationDefinition.setAdditionalKey(AdditionalKey.valueOf(attributeValue.split("\\.")[1]));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.EXIT_ACTION)) {
					navigationDefinition.setExitActionName(StringUtil.toClassName(attributeValue));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.EXIT_ADDITIONAL_KEY)) {
					navigationDefinition.setExitAdditionalKey(AdditionalKey.valueOf(attributeValue.split("\\.")[1]));
				}
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.ASSIGNED_FIELDS)) {
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
						if (pair.getName().equals(ScreenAnnotationConstants.FIELD)) {
							name = StringUtil.stripQuotes(attrValue);
						}
						if (pair.getName().equals(ScreenAnnotationConstants.VALUE)) {
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
				if (memberValuePair.getName().equals(ScreenAnnotationConstants.IDENTIFIERS)) {
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
						if (pair.getName().equals(ScreenAnnotationConstants.ROW)) {
							row = Integer.parseInt(attrValue);
						}
						if (pair.getName().equals(ScreenAnnotationConstants.COLUMN)) {
							column = Integer.parseInt(attrValue);
						}
						if (pair.getName().equals(ScreenAnnotationConstants.VALUE)) {
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
