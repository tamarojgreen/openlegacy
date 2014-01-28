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
package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openlegacy.terminal.FieldAttributeType;

/**
 * Define a Java field as a screen table column. The field containing class
 * should be marked with {@link ScreenTable} annotation<br/>
 * <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * <code>@ScreenColumn(startColumn = 65, endColumn = 68, key = true) <br/>private Integer itemNumber;</code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenColumn {

	boolean key() default false;

	boolean selectionField() default false;

	boolean mainDisplayField() default false;

	int startColumn();

	int endColumn();

	boolean editable() default false;

	String displayName() default AnnotationConstants.NULL;

	String sampleValue() default "";

	int rowsOffset() default 0;

	String helpText() default "";

	int colSpan() default 1;

	/**
	 * Defines the sorting index as display on the original screen
	 */
	int sortIndex() default -1;

	FieldAttributeType attribute() default FieldAttributeType.Value;
}
