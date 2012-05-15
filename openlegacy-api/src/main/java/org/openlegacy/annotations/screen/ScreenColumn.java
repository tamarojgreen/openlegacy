package org.openlegacy.annotations.screen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the field is a screen table column. The field containing should be marked with {@link ScreenTable} annotation<br/>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenColumn {

	boolean key() default false;

	boolean selectionField() default false;

	boolean mainDisplayField() default false;

	int startColumn();

	int endColumn();

	boolean editable() default false;

	String displayName() default "";

	String sampleValue() default "";
}
