package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a terminal screen identifier. This annotation should be declared only
 * within a @ScreenEntity annotation
 * 
 * <code>
 * 
 * @ScreenEntity(identifiers = {
 * @Identifier(row = 1, column = 36, value =
 *                 "Expected text in position 1,36 on screen")
 * @Identifier(row = 2, column = 27, value =
 *                 "Expected text in position 2,27 on screen") }) </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Identifier {

	int row();

	int column();

	String value();

}
