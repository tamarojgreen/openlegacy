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

import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define navigation information for the marked screen entity class. Screen must be marked with {@link ScreenEntity} annotation as
 * well.<br/>
 * <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * <code>@ScreenEntity<br/>@ScreenIdentifiers(... })<br/>@ScreenNavigation(accessedFrom = InventoryManagement.class, assignedFields = { @AssignedField(field = "selection", value = "1") }, exitAction = F3.class)<br/>public class ItemsList{<br/>...<br/>} </code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenNavigation {

	/**
	 * The screen entity class which the marked screen is accessible from
	 * 
	 * @return
	 */
	Class<?> accessedFrom();

	/**
	 * {@link TerminalAction} to execute to reach from the accessed screen. Default to ENTER action.
	 * 
	 * @return terminal action for navigation
	 */
	Class<? extends TerminalAction> terminalAction() default TerminalActions.ENTER.class;

	/**
	 * {@link AdditionalKey} to use in addition to the terminalAction. Default to NONE
	 * 
	 * @return additional key for navigation
	 */
	AdditionalKey additionalKey() default AdditionalKey.NONE;

	/**
	 * A list of static field values to send in order to reach from the source screen to this screen
	 * 
	 * @return array of assigned fields for navigation
	 */
	AssignedField[] assignedFields() default {};

	/**
	 * A {@link TerminalAction} to perform in order to exit the current screen
	 * 
	 * @return
	 */
	Class<? extends TerminalAction> exitAction() default DefaultNavigationExitAction.class;

	/**
	 * {@link AdditionalKey} to use in addition to the exitAction
	 * 
	 * @return exit additional key for navigation
	 */
	AdditionalKey exitAdditionalKey() default AdditionalKey.NONE;

	/**
	 * Specify whether the screen requires dynamic parameter in order to access it. Used to determine whether this screen can
	 * participate in {@link Menu} building.
	 * 
	 * @return whether the screen requires parameters for navigation
	 */
	boolean requiresParameters() default false;

	String drilldownValue() default "";

	public static class AnyScreen {

	}

	public static class DefaultNavigationExitAction implements TerminalAction, Serializable {

		public void perform(TerminalSession session, Object entity, Object... keys) {
			TerminalAction action = session.getModule(Navigation.class).getDefaultExitAction();
			session.doAction(action, (org.openlegacy.terminal.ScreenEntity)entity);
		}

		public String getName() {
			// dummy method to avoid json serialization error
			return getClass().getSimpleName();
		}
	}

}
