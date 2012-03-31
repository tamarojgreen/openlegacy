package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the field is an terminal screen field. This annotation is applied to classes marked as @ScreenEntity
 * 
 * <code>
 * 
 * @FieldMapping(row = 6, column = 18) private String user </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenDateField {

	int yearColumn() default 0;

	int monthColumn() default 0;

	int dayColumn() default 0;

}
