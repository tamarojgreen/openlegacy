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

/**
 * Defines that a Java field is an terminal screen numeric field. This annotation applied to {@link Number} based Java fields
 * (Integer, Double) already marked with {@link ScreenField} annotation. The annotation determine <code>minimumValue</code> and
 * <code>maximumValue</code> meta-data for pages. <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * 
 * <code>@ScreenNumericField(minimumValue= 16, maximumValue = 120)<br/>@ScreenField(row = 20, column = 33, editable = true) <br/>private Integer bankAccountOpenerAge; </code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenNumericField {

	double minimumValue() default Double.MIN_VALUE;

	double maximumValue() default Double.MAX_VALUE;

	/**
	 * Pattern for display
	 * 
	 * @return
	 */
	String pattern() default "#";

}
