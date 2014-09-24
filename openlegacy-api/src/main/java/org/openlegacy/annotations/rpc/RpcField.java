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
package org.openlegacy.annotations.rpc;

import org.openlegacy.FieldType;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.rpc.RpcFieldTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcField {

	String originalName() default "";

	String runtimeName() default "";

	boolean key() default false;

	Direction direction() default Direction.INPUT_OUTPUT;

	int length();

	Class<? extends FieldType> fieldType() default RpcFieldTypes.General.class;

	String displayName() default AnnotationConstants.NULL;

	String sampleValue() default "";

	String helpText() default "";

	boolean editable() default true;

	String defaultValue() default "";

	int order() default AnnotationConstants.AUTOMATICALY;

	String nullValue() default AnnotationConstants.NULL;

	int keyIndex() default 0;

	/**
	 * Calculate the value of the field using a Spring Expression. This makes the field read only. The entity class is the root
	 * context, so you can refer to other fields by their names. Info about the current field is available in the #field variable.
	 * So, you can refer to the value of the current field with #field.value
	 * 
	 * If the expression starts and ends with a /, the expression is processed as a regular expression- not a spring expression
	 * 
	 * @see <a href="http://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring
	 *      Expression Reference</a>
	 * 
	 * @see {@link java.util.regex.Pattern.compile}
	 * @return the Spring expression or regular expression used for generating the value of the field
	 */
	String expression() default "";
}
