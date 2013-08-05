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

	boolean key() default false;

	Direction direction() default Direction.INPUT_OUTPUT;

	int length();

	Class<? extends FieldType> fieldType() default RpcFieldTypes.General.class;

	String displayName() default AnnotationConstants.NULL;

	String sampleValue() default "";

	String helpText() default "";

	boolean editable() default true;

	String defaultValue() default "";
}
