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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines that a {@link Boolean} Java field is an terminal screen boolean field. This annotation applied to Boolean Java fields
 * already marked with {@link ScreenField} annotation. The annotation determine <code>trueValue</code> and <code>falseValue</code>
 * which are sent to the host for according to the field true/false state. When the java field value is null, the field is
 * ignored. <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * 
 * <code>@ScreenBooleanField(trueValue = "Y", falseValue = "N")<br/>@ScreenField(row = 20, column = 33, editable = true) <br/>private Boolean sendInvoice; </code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcBooleanField {

	String trueValue();

	String falseValue();

	/**
	 * Determine whether to set the field to null when the mapped host field position is empty
	 * 
	 * @return whether to treat empty as null
	 */
	boolean treatEmptyAsNull() default false;

}
