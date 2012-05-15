package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the field is a terminal screen numeric field. This annotation is applied to java fields marked with
 * {@link ScreenField} annotation
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenNumericField {

	double minimumValue() default Double.MIN_VALUE;

	double maximumValue() default Double.MAX_VALUE;

}
