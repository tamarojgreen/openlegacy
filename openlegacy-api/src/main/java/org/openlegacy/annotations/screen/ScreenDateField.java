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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * Defines that a Java {@link Date} field is an terminal screen date field. This annotation applied to Date Java fields already
 * marked with {@link ScreenField} annotation. This annotation supports mapping of 3 different columns. Row is determined by
 * {@link ScreenField} annotation
 * 
 * <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * 
 * <code>@ScreenDateField(dayColumn = 34, monthColumn = 37, yearColumn = 40)<br/>@ScreenField(row = 14, column = 34, endColumn = 42, labelColumn = 2, editable = true, displayName = "Amended date", sampleValue= "23") <br/>private Date amendedDate; </code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenDateField {

	int yearColumn() default 0;

	int monthColumn() default 0;

	int dayColumn() default 0;

	String pattern() default "";

	String locale() default "en";

}
