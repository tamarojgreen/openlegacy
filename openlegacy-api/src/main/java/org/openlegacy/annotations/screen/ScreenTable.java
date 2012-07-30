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

import org.openlegacy.modules.table.Table;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.table.ScreenTableCollector;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Define the marked class as a screen table entity. Screens defined as {@link ScreenTable} are scanned and put into
 * {@link ScreenEntitiesRegistry} <br/>
 * A <code>ScreenTable</code> represents a single row, and the containing class holds {@link List} of the table class. <br/>
 * 
 * <br/>
 * 
 * @see Table
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenTable {

	int startRow();

	int endRow();

	String name() default "";

	Class<? extends TerminalAction> nextScreenAction() default TerminalActions.PAGEDOWN.class;

	Class<? extends TerminalAction> previousScreenAction() default TerminalActions.PAGEUP.class;

	boolean supportTerminalData() default false;

	boolean scrollable() default true;

	@SuppressWarnings("rawtypes")
	Class<? extends ScreenTableCollector> tableCollector() default ScreenTableCollector.class;
}
