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
package org.openlegacy.designtime.utils;

import org.openlegacy.utils.StringUtil;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;

import java.util.ArrayList;
import java.util.List;

public class JavaParserUtil {

	public static String getAnnotationValue(AnnotationExpr annotation, String attributeName) {
		if (!(annotation instanceof NormalAnnotationExpr)) {
			return null;
		}
		List<MemberValuePair> attributes = ((NormalAnnotationExpr)annotation).getPairs();
		if (attributes == null) {
			return null;
		}
		for (MemberValuePair memberValuePair : attributes) {
			if (memberValuePair.getName().equals(attributeName)) {
				String value = memberValuePair.getValue().toString();
				if (value.startsWith("\"") && value.endsWith("\"")) {
					value = StringUtil.stripQuotes(value);
				}
				return value;
			}
		}
		return null;

	}

	public static int getAnnotationValueInt(AnnotationExpr annotation, String attributeName, int defaultValue) {
		if (!(annotation instanceof NormalAnnotationExpr)) {
			return defaultValue;
		}
		List<MemberValuePair> attributes = ((NormalAnnotationExpr)annotation).getPairs();
		if (attributes == null) {
			return defaultValue;
		}
		for (MemberValuePair memberValuePair : attributes) {
			if (memberValuePair.getName().equals(attributeName)) {
				int value = Integer.parseInt(memberValuePair.getValue().toString());
				return value;
			}
		}
		return defaultValue;
	}

	public static boolean isOneOfAnnotationsPresent(AnnotationExpr annotation, String... annotationNames) {
		for (String annotationName : annotationNames) {
			if (annotation.getName().getName().equals(annotationName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasAnnotation(AnnotationExpr annotationExpr, String annotation) {
		return annotationExpr.getName().getName().equals(annotation);
	}

	public static String findAnnotationAttribute(String annotationName, List<MemberValuePair> pairs) {
		if (pairs == null) {
			return null;
		}
		for (MemberValuePair memberValuePair : pairs) {
			if (memberValuePair.getName().equals(annotationName)) {
				return memberValuePair.getValue().toString();
			}
		}
		return null;
	}

	public static boolean hasAnnotation(CompilationUnit compilationUnit, String... annotations) {
		List<TypeDeclaration> types = compilationUnit.getTypes();
		if (types == null || types.size() == 0) {
			return false;
		}
		List<AnnotationExpr> annotationExpressions = types.get(0).getAnnotations();
		if (annotationExpressions != null) {
			for (AnnotationExpr annotationExpr : annotationExpressions) {
				for (String annotation : annotations) {
					if (JavaParserUtil.hasAnnotation(annotationExpr, annotation)) {
						return true;
					}
				}
			}
		}
		return false;

	}

	public static List<String> findAnnotationStringAttributes(String annotationName, List<MemberValuePair> pairs) {
		if (pairs == null) {
			return null;
		}
		for (MemberValuePair memberValuePair : pairs) {
			if (memberValuePair.getName().equals(annotationName)) {
				Expression expression = memberValuePair.getValue();
				if (expression instanceof ArrayInitializerExpr) {
					List<String> list = new ArrayList<String>();
					List<Expression> values = ((ArrayInitializerExpr)expression).getValues();
					for (Expression expr : values) {
						if (expr instanceof StringLiteralExpr) {
							list.add(((StringLiteralExpr)expr).getValue());
						}
					}
					return list;
				}
			}
		}
		return null;
	}
}
