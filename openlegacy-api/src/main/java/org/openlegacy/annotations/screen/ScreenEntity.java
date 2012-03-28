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
 * Specifies that the class is a screen entity. Screens defined as @ScreenEntity are scanned and put into ScreenEntitiesRegistry <br/>
 * <br/>
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
