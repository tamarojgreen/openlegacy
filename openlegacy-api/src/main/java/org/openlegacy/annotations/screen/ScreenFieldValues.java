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
 * Specifies that the field has values list from another screen entity defined as {@link LookupEntity} type. This annotation
 * triggers OpenLegacy to generate a method to the {@link ScreenEntity}, get&lt;PropertyName&gt;Values()
 * 
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenFieldValues {

	Class<? extends ScreenRecordsProvider> provider() default ScreenRecordsProvider.class;

	Class<?> sourceScreenEntity();

	boolean collectAll() default false;
}
