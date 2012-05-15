package org.openlegacy.annotations.screen;

import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a part of a screen. Screen parts defined are scanned and put into {@link ScreenEntitiesRegistry} <br/>
 * <br/>
 * A screen part definition defines a repeatable class with mappings which can belongs to a 1 or more screens. A screen is also
 * helpful to break a screen entity with many fields into smaller pieces.
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenPart {

	boolean supportTerminalData() default false;

	String name() default "";
}
