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
package org.openlegacy.designtime.generators;

import static org.openlegacy.designtime.utils.JavaParserUtil.getAnnotationValue;

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleBooleanFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleEnumFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;
import org.openlegacy.rpc.definitions.support.SimpleRpcNumericFieldTypeDefinition;
import org.openlegacy.support.SimpleDisplayItem;
import org.openlegacy.utils.StringConstants;
import org.openlegacy.utils.StringUtil;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.Expression;

import java.util.List;

public class AnnotationsParserUtils {

	public static FieldTypeDefinition loadDateField(AnnotationExpr annotationExpr) {
		Integer yearColumn = null;
		Integer monthColumn = null;
		Integer dayColumn = null;
		String pattern = null;
		String annotationValue = getAnnotationValue(annotationExpr, AnnotationConstants.YEAR_COLUMN);

		if (annotationValue != null) {
			yearColumn = Integer.valueOf(annotationValue);
		}
		annotationValue = getAnnotationValue(annotationExpr, AnnotationConstants.MONTH_COLUMN);
		if (annotationValue != null) {
			monthColumn = Integer.valueOf(annotationValue);
		}
		annotationValue = getAnnotationValue(annotationExpr, AnnotationConstants.DAY_COLUMN);
		if (annotationValue != null) {
			dayColumn = Integer.valueOf(annotationValue);
		}
		annotationValue = getAnnotationValue(annotationExpr, AnnotationConstants.PATTERN);
		pattern = annotationValue != null ? annotationValue : "";

		FieldTypeDefinition dateFieldDefiniton = new SimpleDateFieldTypeDefinition(dayColumn, monthColumn, yearColumn, pattern);
		return dateFieldDefiniton;

	}

	public static FieldTypeDefinition loadBooleanField(AnnotationExpr annotationExpr) {
		String trueValue = getAnnotationValue(annotationExpr, AnnotationConstants.TRUE_VALUE);
		String falseValue = getAnnotationValue(annotationExpr, AnnotationConstants.FALSE_VALUE);
		String treatNullAsEmpty = getAnnotationValue(annotationExpr, AnnotationConstants.TREAT_EMPTY_AS_NULL);
		FieldTypeDefinition booleanFieldDefiniton = new SimpleBooleanFieldTypeDefinition(StringUtil.stripQuotes(trueValue),
				StringUtil.stripQuotes(falseValue), StringConstants.TRUE.equals(treatNullAsEmpty));
		return booleanFieldDefiniton;
	}

	/**
	 * @param mainType
	 * @param fieldDeclaration
	 * @param field
	 */
	public static FieldTypeDefinition loadEnumField(ClassOrInterfaceDeclaration mainType, FieldDeclaration fieldDeclaration) {
		if (mainType == null) {
			return null;
		}
		String fieldType = fieldDeclaration.getType().toString();

		SimpleEnumFieldTypeDefinition enumDefinition = null;
		List<BodyDeclaration> members = mainType.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof EnumDeclaration) {
				EnumDeclaration enumDeclaration = (EnumDeclaration)bodyDeclaration;
				if (enumDeclaration.getName().equals(fieldType)) {
					enumDefinition = new SimpleEnumFieldTypeDefinition();

					List<EnumConstantDeclaration> entries = enumDeclaration.getEntries();
					if (entries != null) {

						for (EnumConstantDeclaration entry : entries) {
							String value = "";
							String displayName = "";

							List<Expression> args = entry.getArgs();
							if (args != null) {
								if (args.size() == 1) {
									value = StringUtil.stripQuotes(args.get(0).toString());
								} else if (args.size() >= 2) {
									value = StringUtil.stripQuotes(args.get(0).toString());
									displayName = StringUtil.stripQuotes(args.get(1).toString());
								}
								enumDefinition.getEnums().put(entry.getName(), new SimpleDisplayItem(value, displayName));
							}
						}
					}
					break;
				}
			}
		}
		return enumDefinition;
	}

	public static FieldTypeDefinition loadRpcNumericField(AnnotationExpr annotationExpr) {
		String minimumValueFromAnnotation = getAnnotationValue(annotationExpr, RpcAnnotationConstants.MINIMUM_VALUE);
		String maximumValueFromAnnotation = getAnnotationValue(annotationExpr, RpcAnnotationConstants.MAXIMUM_VALUE);
		String decimalPlacesFromAnnotation = getAnnotationValue(annotationExpr, RpcAnnotationConstants.DECIMAL_PLACES);
		String patternFromAnnotation = getAnnotationValue(annotationExpr, RpcAnnotationConstants.NUMERIC_PATTERN);

		double minimumValue = !StringUtil.isEmpty(minimumValueFromAnnotation) ? Double.valueOf(minimumValueFromAnnotation) : 0.0;
		double maximumValue = !StringUtil.isEmpty(maximumValueFromAnnotation) ? Double.valueOf(maximumValueFromAnnotation) : 0.0;
		int decimalPlaces = !StringUtil.isEmpty(decimalPlacesFromAnnotation) ? Integer.valueOf(decimalPlacesFromAnnotation) : 0;
		String pattern = !StringUtil.isEmpty(patternFromAnnotation) ? patternFromAnnotation : "#";

		FieldTypeDefinition fieldTypeDefinition = new SimpleRpcNumericFieldTypeDefinition(minimumValue, maximumValue,
				decimalPlaces, pattern);
		return fieldTypeDefinition;
	}

	public static FieldTypeDefinition loadNumericField(AnnotationExpr annotationExpr) {
		String minimumValueFromAnnotation = getAnnotationValue(annotationExpr, RpcAnnotationConstants.MINIMUM_VALUE);
		String maximumValueFromAnnotation = getAnnotationValue(annotationExpr, RpcAnnotationConstants.MAXIMUM_VALUE);
		String patternFromAnnotation = getAnnotationValue(annotationExpr, RpcAnnotationConstants.NUMERIC_PATTERN);

		double minimumValue = !StringUtil.isEmpty(minimumValueFromAnnotation) ? Double.valueOf(minimumValueFromAnnotation) : 0.0;
		double maximumValue = !StringUtil.isEmpty(maximumValueFromAnnotation) ? Double.valueOf(maximumValueFromAnnotation) : 0.0;
		String pattern = !StringUtil.isEmpty(patternFromAnnotation) ? patternFromAnnotation : "#";

		FieldTypeDefinition fieldTypeDefinition = new SimpleNumericFieldTypeDefinition(minimumValue, maximumValue, pattern);
		return fieldTypeDefinition;
	}
}
