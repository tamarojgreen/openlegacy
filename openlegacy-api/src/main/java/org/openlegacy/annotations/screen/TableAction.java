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

import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;

/**
 * A table action for a screen table entity. Defined within {@link ScreenTableActions}<br/>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TableAction {

	Class<? extends TerminalAction> action() default ENTER.class;

	boolean defaultAction() default false;

	String actionValue() default "";

	String displayName();

	String alias() default "";

	Class<?> targetEntity() default ScreenEntity.NONE.class;

	/**
	 * Along with column, length, when - specify content in row,column in given length which matched when regex
	 */
	int row() default 0;

	/**
	 * Along with row, length, when - specify content in row,column in given length which matched when regex
	 */
	int column() default 0;

	/**
	 * Along with row, column, when - specify content in row,column in given length which matched when regex
	 */
	int length() default 0;

	/**
	 * A regex condition that defines a certain text pattern on the screen in which to include the action. Need to define row,
	 * column, length to be affective
	 * 
	 * @return
	 */
	String when() default ".*";
}
