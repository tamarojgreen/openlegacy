package org.openlegacy.annotations.screen;

import org.openlegacy.FieldType;

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
public @interface ScreenField {

	int row();

	int column();

	boolean editable() default false;

	int length() default 0;

	Class<? extends FieldType> fieldType() default FieldType.General.class;

	String displayName() default "";

	String sampleValue() default "";
}
