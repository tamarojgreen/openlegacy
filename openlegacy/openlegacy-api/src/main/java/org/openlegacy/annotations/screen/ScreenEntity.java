package org.openlegacy.annotations.screen;

import org.openlegacy.HostEntityType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a screen entity. Screens defined as @ScreenEntity are scanned and put into ScreenEntitiesRegistry <br/>
 * <br/>
 * Option 1: When using RegistryBasedScreensRecognizer <code>
 * 
 * @ScreenEntity(identifiers = { @Identifier(row = 1, column = 36, value = "Sign On") }) </code> <br/>
 * <br/>
 * 
 * Option 2: When using a vendor ScreenRecognizer <code>
 * @ScreenEntity </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenEntity {

	String name() default "";

	boolean supportTerminalData() default false;

	Identifier[] identifiers() default {};

	Class<? extends HostEntityType> screenType() default HostEntityType.General.class;
}
