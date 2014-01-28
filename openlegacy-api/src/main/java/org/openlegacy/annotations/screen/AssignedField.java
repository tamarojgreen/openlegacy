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
 * Defines an constant field assignment. Currently used within {@link ScreenNavigation} annotation. Typical usage would be within
 * moving from screen 1 to screen 2 using a constant argumnt.
 * 
 * <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * 
 * <code>@ScreenNavigation(accessedFrom = InventoryManagement.class, assignedFields = { @AssignedField(field = "selection", value ="1")})<br/>public class ItemDetails {<br/>...<br/>} </code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AssignedField {

	String field();

	// marking with "fake" null since default can't be null. Handled in ScreenNavigationAnnotationLoader
	String value() default AnnotationConstants.NULL;

}
