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
package org.openlegacy.definitions;

import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;
import org.openlegacy.annotations.screen.ScreenFieldValues;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

/**
 * Defines a field with values field type registry information. A field with values definition contains definition for field with
 * values field, including the records provider, collect all, and source entity, if available. Typically created from
 * {@link ScreenFieldValues} annotation. Will be typically displays as auto complete, or lookup window
 * 
 * @author Roi Mor
 */
public interface FieldWithValuesTypeDefinition extends FieldTypeDefinition {

	<S extends Session, T> RecordsProvider<S, T> getRecordsProvider();

	boolean isCollectAll();

	Class<?> getSourceEntityClass();

	/**
	 * The source entity class name, Used for design-time purposes
	 * 
	 * @return source entity class name
	 */
	String getSourceEntityClassName();

	/**
	 * Used for design-time purposes
	 * 
	 * @return
	 */
	ScreenEntityDefinition getSourceEntityDefinition();

	String getDisplayFieldName();

	boolean isAsWindow();
}