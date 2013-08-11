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

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Action;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Field;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
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

public class RpcAnnotationsParserUtils {

	public static void loadRpcFieldAnnotation(AnnotationExpr annotationExpr, Field field) {
		String editableValue = getAnnotationValue(annotationExpr, AnnotationConstants.EDITABLE);
		String displayNameValue = getAnnotationValue(annotationExpr, AnnotationConstants.DISPLAY_NAME);
		String helpTextValue = getAnnotationValue(annotationExpr, AnnotationConstants.HELP_TEXT);
		String keyValue = getAnnotationValue(annotationExpr, AnnotationConstants.KEY);
		String mainDisplayFieldValue = getAnnotationValue(annotationExpr, AnnotationConstants.MAIN_DISPLAY_FIELD);
		String fieldTypeName = getAnnotationValue(annotationExpr, AnnotationConstants.FIELD_TYPE);
		String passwordValue = getAnnotationValue(annotationExpr, AnnotationConstants.PASSWORD);
		String sampleValue = getAnnotationValue(annotationExpr, AnnotationConstants.SAMPLE_VALUE);
		String rightToLeftValue = getAnnotationValue(annotationExpr, AnnotationConstants.RIGHT_TO_LEFT);
		String lengthValue = getAnnotationValue(annotationExpr, AnnotationConstants.LENGTH);
		String runtimeName = getAnnotationValue(annotationExpr, RpcAnnotationConstants.RUNTIME_NAME);
		String direction = getAnnotationValue(annotationExpr, RpcAnnotationConstants.DIRECTION);

		field.setLength(Integer.valueOf(lengthValue));

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
		if (runtimeName != null) {
			field.setRuntimeName(StringUtil.stripQuotes(runtimeName));
		}
		if (direction != null) {
			field.setDirection(Direction.valueOf(direction));
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

				Action action = new Action(actionAlias, actionClassName, displayName);
				actions.add(action);
			}
		}
		return actions;
	}

}
