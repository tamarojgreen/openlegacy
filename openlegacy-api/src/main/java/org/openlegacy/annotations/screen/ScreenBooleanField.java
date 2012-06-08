package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the field is an terminal screen boolean field. This annotation is applied to java fields marked as
 * {@link ScreenField} annotation
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenBooleanField {

	String trueValue();

	String falseValue();

	boolean treatEmptyAsNull() default false;

}
