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

import org.openlegacy.terminal.services.ScreenEntitiesRegistry;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the marked class as a part of a screen entity. Screen parts defined are scanned and put into
 * {@link ScreenEntitiesRegistry} <br/>
 * <br/>
 * A screen part definition defines a repeatable class with mappings which can belongs to a 1 or more screens. A screen is also
 * helpful to break a screen entity with many fields into smaller parts. <br/>
 * <br/>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenPart {

	boolean supportTerminalData() default false;

	String name() default "";
}
