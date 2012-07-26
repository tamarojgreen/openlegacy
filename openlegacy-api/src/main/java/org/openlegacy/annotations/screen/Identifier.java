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

/**
 * Defines a terminal screen identifier. A set of identifiers on {@link ScreenIdentifiers} annotation, define a unique identifier
 * for a snapshot screen, and make it matched to a given a class marked with {@link ScreenEntity} annotation.
 * 
 * <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * <code>@ScreenEntity<br/>@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 36, value = "Expected text in position 1,36 on screen"),@Identifier(row = 2, column = 27, value = "Expected text in position 2,27 on screen") })<br/>public class ItemDetails{<br/>...<br/>} </code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Identifier {

	int row();

	int column();

	String value();

}
