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

import org.openlegacy.EntityType;
import org.openlegacy.terminal.ScreenEntityType;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreensRecognizer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A central annotation, which defines the marked Java class as a screen entity. A screen entity is a strong type Java class,
 * which is mapped to a {@link TerminalSnapshot} typically using {@link ScreenIdentifiers}, or a custom {@link ScreensRecognizer}
 * defines which the Spring application context.
 * 
 * Screen classes defined with this annotation are scanned and put into {@link ScreenEntitiesRegistry}. <br/>
 * <br/>
 * The registry must be defined as a spring bean, and a List of packages (typically 1) should be defined within it to find the
 * marked classes. <br/>
 * <br/>
 * This annotation is also used in development phase to automatically generate a matching AspectJ, which adds additional
 * interfaces, methods and fields to the class, <br/>
 * like getters and setters (if not defined), focus field (which is needed for most screens), and optionally
 * {@link TerminalSnapshot} and {@link TerminalField} getters for each of the fields, if supportedTerminalData is set to true.
 * 
 * Example:<br/>
 * <br/>
 * <code>@ScreenEntity<br/>@ScreenIdentifiers(...)<br/> public class apps.inventory.screens.ItemDetails {<br/> ...<br/> }<br/> </code>
 * <br/>
 * Registry definition spring bean example:<br/>
 * <br/>
 * <code>	&lt;bean id="screensRegistry" class="org.openlegacy.terminal.support.DefaultScreenEntitiesRegistry"&gt;<br/>
	    &lt;property name="packages"&gt;<br/>
	        &lt;list&gt;<br/>
	            &lt;value&gt;apps.inventory.screens&lt;/value&gt;<br/>
            &lt;/list&gt;<br/>
	    &lt;/property&gt;<br/>
    </bean>
</code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenEntity {

	/**
	 * The name of the entity. Default to the class name
	 * 
	 * @return the entity name
	 */
	String name() default "";

	/**
	 * The entity display name. translated to friendly label if not defined specifically
	 * 
	 * @return display name
	 */
	String displayName() default "";

	/**
	 * Used in design-time by OpenLegacy design-time tools (eclipse builder and design-time API's) to determine if to generate
	 * access method to terminal data, such as the mapped {@link TerminalField} for each Java field, and an access method to the
	 * mapped {@link TerminalSnapshot}. <br>
	 * Additional <code>getSnapshot</code> method will be added, and <code>get&lt:FIELD-NAME&gt;Field</code> methods (e.g.
	 * getOrderIdField) for each Java field.
	 * 
	 * @return is the screen entity should support terminal data
	 */
	boolean supportTerminalData() default false;

	boolean window() default false;

	Class<? extends EntityType> screenType() default ScreenEntityType.General.class;

	/**
	 * The screen columns width. used for design-time to UI generation. Default to 80 columns.
	 * 
	 * @return screen columns count
	 */
	int columns() default ScreenSize.DEFAULT_COLUMN;

	/**
	 * The screen rows height. used for design-time to UI generation. Default to 24 rows.
	 * 
	 * @return screen rows count
	 */
	int rows() default ScreenSize.DEFAULT_ROWS;

	/**
	 * Determine whether this screen is a child screen of another main screen. Useful for design-time to decide if to generate
	 * different page layout.
	 * 
	 * @return is the screen is a child screen
	 */
	boolean child() default false;

	/**
	 * Determine whether to check screen primary keys correctness. Relevant in case key is not displayed on screen
	 * 
	 * @return whether to check screen primary keys correctness
	 */
	boolean validateKeys() default true;

	boolean rightToLeft() default false;

	boolean autoMapKeyboardActions() default false;

	/**
	 * Since version 2.3.0 attribute is deprecated. For adding entity into menu, add SHOW action to the entity
	 */
	@Deprecated
	String[] roles() default AnnotationConstants.NULL;
}
