package org.openlegacy.annotations.screen;

import org.openlegacy.terminal.definitions.FieldAssignDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A screen entity field assignment. Currently used within {@link ScreenNavigation} annotation
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AssignedField {

	String field();

	// marking with "fake" null since default can't be null. Handled in ScreenNavigationAnnotationLoader
	String value() default FieldAssignDefinition.NULL;

}
