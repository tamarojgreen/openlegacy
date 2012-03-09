package org.openlegacy.annotations.screen;

import org.openlegacy.terminal.ScreenRecordsProvider;

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
public @interface ScreenFieldValues {

	Class<? extends ScreenRecordsProvider> provider() default ScreenRecordsProvider.class;

	Class<?> sourceScreenEntity();

	Class<?> record();

	boolean collectAll() default false;
}
