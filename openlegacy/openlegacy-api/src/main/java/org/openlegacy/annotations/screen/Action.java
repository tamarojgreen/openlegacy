package org.openlegacy.annotations.screen;

import org.openlegacy.terminal.actions.TerminalAction;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A terminal session action for a screen entity. Defined within @ScreenActions<br/>
 * <br/>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Action {

	Class<? extends TerminalAction> action();

	String displayName();

	/**
	 * Not mandatory
	 */
	int row() default 0;

	/**
	 * Not mandatory
	 */
	int column() default 0;
}
