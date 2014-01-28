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

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;

/**
 * Defines that the marked Java field has additional description field in the specified location. 
 * Example:
 * Customer: 123 <b>ABC Inc.</b>
 * <br/>
 * This annotation is applied to classes
 * marked with {@link ScreenEntity} annotation <br/>
 * <br/>
 * 
 * @author Roi Mor
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenDescriptionField {

	/**
	 * The {@link TerminalField} row the field is mapped to
	 * Default to @ScreenField row
	 * @return the field row on the host screen
	 */
	int row() default 0;

	/**
	 * The host field column it the field mapped to
	 * 
	 * @return the field column on the host screen
	 */
	int column();

	/**
	 * Optional. The {@link TerminalField} end column. Used in case doesn't match the host field length by attributes. Also used
	 * for design-time code generation of pages, to determine fields length
	 * 
	 * @return the end column to grab content from the {@link TerminalSnapshot}
	 */
	int endColumn() default 0;

	/**
	 * Optional. A sample value for the field. Use for readability of generated code, and design-time previews
	 * 
	 * @return field sample value
	 */
	String sampleValue() default "";

	boolean rightToLeft() default false;

}
