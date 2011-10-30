package org.openlegacy.annotations.screen;

import org.openlegacy.FetchMode;
import org.openlegacy.HostAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be marked on a child screen entity defined as a field with a parent screen entity
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChildScreenEntity {

	Class<? extends HostAction> stepInto();

	FetchMode fetchMode() default FetchMode.LAZY;
}
