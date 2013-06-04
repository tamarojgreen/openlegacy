package org.openlegacy.annotations.rpc;

import org.openlegacy.FieldType;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.rpc.RpcFieldTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcField {

	String originalName() default "";

	boolean key() default false;

	Direction direction();

	double length();

	Class<? extends FieldType> fieldType() default RpcFieldTypes.General.class;

	String displayName() default AnnotationConstants.NULL;

	String sampleValue() default "";

	String helpText() default "";

	boolean editable() default true;

}
