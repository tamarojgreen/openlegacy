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
 * A container annotation for all {@link Action} annotations which define fixed actions on a class marked with
 * {@link ScreenEntity}<br/>
 * Actions are used as meta-data for exposing possible actions on a screen entity to front-ends / remote API's, An example is
 * OpenLegacy MVC web framework, which automatically generates web controllers handler methods for each action. <br/>
 * 
 * <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * <code>@RpcActions(actions = { @Action(action = RpcActions.UPDATE.class, displayName = "Update",path="/PATH-ON-HOST") }) <br/>public class ItemDetails {<br/>...<br/>}</code>
 * <br/>
 * 
 * @author Roi Mor
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcActions {

	Action[] actions();
}
