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
import java.util.Date;

/**
 * Defines that a Java {@link Date} field is an terminal screen dynamic field. This annotation applied to fields already
 * marked with {@link ScreenField} annotation.
 * 
 * <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * 
 * <code>@ScreenDynamicField(text = "Item Number" , fieldOffset = 1, row = 5, endRow =9, column =1 , endColumn = 70)<br/>@ScreenField(row = 14, column = 34, endColumn = 42, labelColumn = 2, editable = true, displayName = "Amended date", sampleValue= "23") <br/>private Date amendedDate; </code>
 * 
 * @author Gad Salner
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenDynamicField {

	String text();
	
	int row() default 0;

	int column() default 0;

	int endColumn() default 0;

	int endRow() default 0;
	
	int fieldOffset() default -1;

}
