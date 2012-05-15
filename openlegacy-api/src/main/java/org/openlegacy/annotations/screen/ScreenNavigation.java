package org.openlegacy.annotations.screen;

import org.openlegacy.modules.menu.Menu;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies navigation definition for the given screen entity class.
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
	 * {@link TerminalAction} to execute to reach from the accessed screen
	 * 
	 * @return
	 */
	Class<? extends TerminalAction> terminalAction() default TerminalActions.ENTER.class;

	/**
	 * {@link AdditionalKey} to use in addition to the terminalAction
	 * 
	 * @return
	 */
	AdditionalKey additionalKey() default AdditionalKey.NONE;

	/**
	 * A list of static field values to send in order to reach from the source screen to this screen
	 * 
	 * @return
	 */
	AssignedField[] assignedFields() default {};

	/**
	 * A {@link TerminalAction} to perform in order to exit the current screen
	 * 
	 * @return
	 */
	Class<? extends TerminalAction> exitAction() default TerminalActions.F3.class;

	/**
	 * {@link AdditionalKey} to use in addition to the exitAction
	 * 
	 * @return
	 */
	AdditionalKey exitAdditionalKey() default AdditionalKey.NONE;

	/**
	 * Specify whether the screen requires dynamic parameter in order to access it. Used to determine whether this screen can
	 * participate in {@link Menu} building.
	 * 
	 * @return
	 */
	boolean requiresParameters() default false;
}
