package org.openlegacy.annotations.cache;

import org.openlegacy.SessionAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cacheable {

	int expiry() default -1;

	Class<? extends SessionAction<?>>[] getActions();

	// Class<? extends SessionAction<?>>[] putActions() default {};

	Class<? extends SessionAction<?>>[] removeActions() default {};
}
