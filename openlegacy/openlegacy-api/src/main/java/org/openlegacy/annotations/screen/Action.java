package org.openlegacy.annotations.screen;

import org.openlegacy.terminal.actions.TerminalAction;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a screen action.<br/>
 * <br/>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Action {

	Class<? extends TerminalAction> action();

	String displayName();
}
