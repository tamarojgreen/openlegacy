/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.annotations.screen;

import org.openlegacy.FieldType;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A central annotation. Defines that the marked Java field is a terminal screen field. This annotation is applied to classes
 * marked with {@link ScreenEntity} annotation <br/>
 * <br/>
 * Example:<br/>
 * <br/>
 * 
 * <code> @ScreenField(row = 6, column = 53, endColumn = 62, fieldType = Login.UserField.class, editable = true, labelColumn = 17)
 * <br/>
 * private String user;<code>
 * 
 * @author Roi Mor
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenField {

	/**
	 * The {@link TerminalField} row the field is mapped to
	 * 
	 * @return the field row on the host screen
	 */
	int row();

	/**
	 * The host field column it the field mapped to
	 * 
	 * @return the field column on the host screen
	 */
	int column();

	/**
	 * Optional. The {@link TerminalField} end column. Used in case doesn't match the host field length by attributes. Also used
	 * for design-time code generation of pages, to determine fields length
	 * 
	 * @return the end column to grab content from the {@link TerminalSnapshot}
	 */
	int endColumn() default 0;

	/**
	 * Optional. The {@link TerminalField} end row. Used in case the endRow doesn't match the start row
	 * 
	 * @return the end row to grab content from the {@link TerminalSnapshot}
	 */
	int endRow() default 0;

	/**
	 * When the endRow doesn't match the start row, determine whether to grab a rectangle or as breaking lines
	 * 
	 * @return
	 */
	boolean rectangle() default false;

	/**
	 * Optional. Define whether the field is editable. Used to define OpenLegacy eclipse builder and designtime API's whether to
	 * generate. Default false. setter method for this field in the containing class.
	 * 
	 * @return is the field editable
	 */
	boolean editable() default false;

	/**
	 * Optional. Define if this field is a password field. Default false
	 * 
	 * @return is the field a password field
	 */
	boolean password() default false;

	/**
	 * Optional. Define a field applicative type (user, password, error, menu selection, etc). Field types are used by
	 * {@link SessionModule} 's to perform higher level operations on a {@link TerminalSession} such as {@link Login},
	 * {@link Menu}
	 * 
	 * @return the field applicative type
	 */
	Class<? extends FieldType>fieldType() default FieldType.General.class;

	/**
	 * Optional. The field display name. Default to friendly name conversion of the Java field name.
	 * 
	 * @return field display name
	 */
	String displayName() default AnnotationConstants.NULL;

	/**
	 * Optional. A sample value for the field. Use for readability of generated code, and design-time previews
	 * 
	 * @return field sample value
	 */
	String sampleValue() default "";

	/**
	 * Optional. A default value for the field. Use for assignment of value when empty
	 * 
	 * @return field sample value
	 */
	String defaultValue() default AnnotationConstants.NULL;

	/**
	 * Optional. Define the field leading label column. Generated by OpenLegacy analyzer. Used by for design time to generate well
	 * proportioned pages.
	 * 
	 * @return the leading label column
	 */
	int labelColumn() default 0;

	/**
	 * define if the field is the key field of the parent screen entity containing class. default false. Usage is for giving
	 * unique Id's to page panel based on the keys field. TODO Future use will be for strict navigation not only by the screen,
	 * but also including the screen keys.
	 * 
	 * @return if the field is the key field
	 */
	boolean key() default false;

	int keyIndex() default -1;

	String helpText() default "";

	boolean rightToLeft() default false;

	/**
	 * Defines the terminal field attributes to bind the Java field to. Default is Value
	 * 
	 * @return the attribute to bind to
	 */
	FieldAttributeType attribute() default FieldAttributeType.Value;

	/**
	 * Optional. Define the field filter.
	 * 
	 * @return the regular expression used for filtering
	 */
	String when() default "";

	/**
	 * Optional. Define the field negative filter.
	 * 
	 * @return the regular expression used for filtering
	 */
	String unless() default "";

	boolean internal() default false;

	boolean global() default false;

	String nullValue() default AnnotationConstants.NULL;

	/**
	 * Determine whether the field should be passed a parameter as table key
	 * 
	 * @return
	 */
	boolean tableKey() default false;

	boolean forceUpdate() default false;

	/**
	 * Calculate the value of the field using a Spring Expression. This makes the field read only. The entity class is the root
	 * context, so you can refer to other fields by their names. Info about the current field is available in the #field variable.
	 * So, you can refer to the value of the current field with #field.value
	 * 
	 * If the expression starts and ends with a /, the expression is processed as a regular expression- not a spring expression
	 * 
	 * @see <a href="http://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring
	 *      Expression Reference</a>
	 * 
	 * @see {@link java.util.regex.Pattern.compile}
	 * @return the Spring expression or regular expression used for generating the value of the field
	 */
	String expression() default "";

	boolean enableLookup() default false;

	Class<? extends TerminalAction>lookupAction() default TerminalActions.NONE.class;

	String regularExpression() default "";

	boolean required() default false;

	String invalidMessage() default "";

	String[] roles() default AnnotationConstants.NULL;

}