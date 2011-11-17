package org.openlegacy.annotations.screen;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.actions.SendKeyClasses;
import org.openlegacy.terminal.table.ScreenTableCollector;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a screen entity. Screens defined as @ScreenEntity are scanned and put into ScreenEntitiesRegistry <br/>
 * <br/>
 * 
 * @ScreenEntity </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenTable {

	int startRow();

	int endRow();

	Class<? extends HostAction> nextScreenAction() default SendKeyClasses.PAGEDOWN.class;

	Class<? extends HostAction> previousScreenAction() default SendKeyClasses.PAGEUP.class;

	boolean supportTerminalData() default false;

	boolean scrollable() default true;

	@SuppressWarnings("rawtypes")
	Class<? extends ScreenTableCollector> tableCollector() default ScreenTableCollector.class;
}
