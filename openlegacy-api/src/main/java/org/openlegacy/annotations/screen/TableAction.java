package org.openlegacy.annotations.screen;

import org.openlegacy.terminal.modules.table.TerminalDrilldownActions.EnterDrilldownAction;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

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
public @interface TableAction {

	Class<? extends TerminalDrilldownAction> action() default EnterDrilldownAction.class;

	String actionValue();

	String displayName();

	String alias() default "";
}
