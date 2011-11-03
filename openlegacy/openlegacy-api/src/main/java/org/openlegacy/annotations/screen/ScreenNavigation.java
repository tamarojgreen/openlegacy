package org.openlegacy.annotations.screen;

import org.openlegacy.HostAction;
import org.openlegacy.terminal.actions.SendKeyClasses;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenNavigation {

	Class<?> accessedFrom();

	Class<? extends HostAction> hostAction() default SendKeyClasses.ENTER.class;

	AssignedField[] assignedFields() default {};

	Class<? extends HostAction> exitAction();
}
