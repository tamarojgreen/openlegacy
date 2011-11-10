package org.openlegacy.annotations.screen;

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

	boolean supportTerminalData() default false;

}
