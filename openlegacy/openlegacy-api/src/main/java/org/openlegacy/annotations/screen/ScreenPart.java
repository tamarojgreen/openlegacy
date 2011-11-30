package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a screen part. Screens defined as @ScreenEntity are scanned and put into ScreenEntitiesRegistry <br/>
 * <br/>
 * A screen part definition defines a repeatable class with mappings which can belongs to a 1 or more screens
 * 
 * @ScreenEntity </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenPart {

	boolean supportTerminalData() default false;
}
