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
package org.openlegacy.annotations.screen;

import org.openlegacy.FieldType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the field is an terminal screen field. This annotation is applied to classes marked with {@link ScreenEntity}
 * annotation
 * 
 * <code>
 * 
 * @ScreenField (row = 6, column = 18) private String user </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenField {

	int row();

	int column();

	int endColumn() default 0;

	boolean editable() default false;

	boolean password() default false;

	Class<? extends FieldType> fieldType() default FieldType.General.class;

	String displayName() default "";

	String sampleValue() default "";

	int labelColumn() default 0;

	boolean key() default false;
}
