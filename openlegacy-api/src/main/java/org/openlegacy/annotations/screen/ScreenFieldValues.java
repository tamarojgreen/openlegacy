/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.annotations.screen;

import org.openlegacy.modules.table.LookupEntity;
import org.openlegacy.terminal.ScreenRecordsProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define that the field has values list from another screen entity defined as {@link LookupEntity} screen type. This annotation
 * is triggering generation of method to the {@link ScreenEntity}, get&lt;PropertyName&gt;Values()
 * 
 * <br>
 * </br> Example:<br/>
 * <br/>
 * <code>@ScreenFieldValues(sourceScreenEntity = WarehouseTypes.class)<br/>@ScreenField(row = 8, column = 34, endColumn = 35, labelColumn = 2, editable = true, displayName = "Warehouse Type", sampleValue = "GL")<br/>
	private String warehouseType;
</code>
 * 
 * @author Roi Mor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenFieldValues {

	Class<? extends ScreenRecordsProvider> provider() default ScreenRecordsProvider.class;

	Class<?> sourceScreenEntity();

	boolean collectAll() default false;

	/**
	 * Whether to show the field as auto-complete (default) or lookup window
	 * 
	 * @return
	 */
	boolean asWindow() default false;

	/**
	 * When asWindow = true, used for displaying the mainDisplayField of the window lookup table
	 * 
	 * @return display field name
	 */
	String displayFieldName() default "";

}
