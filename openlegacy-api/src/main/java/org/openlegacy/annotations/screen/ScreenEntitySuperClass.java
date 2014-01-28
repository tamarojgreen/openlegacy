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
 * Defines that the marked class is a screen entity abstract class. This annotation allows reusing screen entities definitions
 * using inheritance.<br/>
 * Once defined, all annotations applied to classes defined with {@link ScreenEntity} are applying the relevant class definitions. <br/>
 * This means it is applicable for <code>org.openlegacy.annotations.screen.*</code> annotations.<br/>
 * 
 * <br/>
 * Example:<br/>
 * <br/>
 * <code>@ScreenEntitySuperClass<br/>@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 48, value = "System") })<br/>@ScreenActions(actions = { @Action(action = TerminalActions.ENTER.class, displayName = "Enter") }) <br/>public abstract class
 *                        AbstractPurchasingScreen {<br/>...<br/>} </code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenEntitySuperClass {

	boolean supportTerminalData() default false;

}
