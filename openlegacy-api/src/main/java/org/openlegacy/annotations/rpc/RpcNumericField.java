package org.openlegacy.annotations.rpc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcNumericField {

	double minimumValue() default 0.0;

	double maximumValue() default 0.0;

	int decimalPlaces() default 0;

}
