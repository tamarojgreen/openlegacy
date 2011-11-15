package org.openlegacy.utils;

import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.util.List;

public class JavaParserUtil {

	public static String getAnnotationValue(AnnotationExpr annotation, String attributeName) {
		if (!(annotation instanceof NormalAnnotationExpr)) {
			return null;
		}
		List<MemberValuePair> attributes = ((NormalAnnotationExpr)annotation).getPairs();
		for (MemberValuePair memberValuePair : attributes) {
			if (memberValuePair.getName().equals(attributeName)) {
				return memberValuePair.getValue().toString();
			}
		}
		return null;

	}

	public static boolean isOneOfAnnotationsPresent(AnnotationExpr annotation, String... annotationNames) {
		for (String annotationName : annotationNames) {
			if (annotation.getName().getName().equals(annotationName)) {
				return true;
			}
		}
		return false;
	}

}
