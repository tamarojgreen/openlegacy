package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class contains screen identifiers.<br/>
 * <br/>
 * <code>
 * 
 * @ScreenIdentifiers (identifiers = { @Identifier(row = 1, column = 36, value = "Sign On") }) </code> <br/>
 * <br/>
 * 
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenIdentifiers {

	Identifier[] identifiers() default {};
}
