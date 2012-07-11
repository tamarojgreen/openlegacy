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

import org.openlegacy.EntityType;
import org.openlegacy.terminal.ScreenEntityType;
import org.openlegacy.terminal.ScreenSize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a screen entity. Screens defined with this annotation Entity are scanned and put into
 * ScreenEntitiesRegistry. <br/>
 * <br/>
 * Scanning must be defined in spring context using &lt;context:component-scan base-package="&lt;PACKAGE&gt;" /&gt;
 * 
 * @ScreenEntity </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenEntity {

	String name() default "";

	String displayName() default "";

	boolean supportTerminalData() default false;

	boolean window() default false;

	Class<? extends EntityType> screenType() default ScreenEntityType.General.class;

	int columns() default ScreenSize.DEFAULT_COLUMN;

	int rows() default ScreenSize.DEFAULT_ROWS;

	boolean child() default false;
}
