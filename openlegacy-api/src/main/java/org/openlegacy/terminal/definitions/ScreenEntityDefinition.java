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
package org.openlegacy.terminal.definitions;

import org.openlegacy.EntityDefinition;
import org.openlegacy.annotations.screen.ScreenActions;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreenIdentification;

import java.util.List;
import java.util.Map;

/**
 * Defines a screen entity definitions stored in the <code>ScreenEntitiesRegistry</code> Typically collected from
 * {@link ScreenEntity}, {@link ScreenIdentifiers}, {@link ScreenNavigation}, {@link ScreenActions}, {@link ScreenField}
 * annotations
 * 
 * @author Roi Mor
 */
public interface ScreenEntityDefinition extends EntityDefinition<ScreenFieldDefinition> {

	ScreenIdentification getScreenIdentification();

	NavigationDefinition getNavigationDefinition();

	/**
	 * field name -> table definition
	 * 
	 * @return map of table definitions of the screen entity
	 */
	Map<String, ScreenTableDefinition> getTableDefinitions();

	/**
	 * field name -> part definition
	 * 
	 * @return map of screen parts definitions of the screen entity
	 */
	Map<String, ScreenPartEntityDefinition> getPartsDefinitions();

	TerminalSnapshot getSnapshot();

	/**
	 * Used for design-time. Contains the original snapshot without analyzer manipulations
	 */
	TerminalSnapshot getOriginalSnapshot();

	/**
	 * Whether the screen entity is a window. gets higher priority in screen recognition process
	 * 
	 * @return is the screen entity is a window
	 */
	boolean isWindow();

	/**
	 * Whether the screen entity is a child entity. Used for determine the page type to generate
	 * 
	 * @return is the screen entity is a child entity
	 */
	boolean isChild();

	/**
	 * Gets the screen entity definitions which this screen is accessed from
	 * 
	 * @return screen entity definitions of the accessed from screen
	 */
	ScreenEntityDefinition getAccessedFromScreenDefinition();

	/**
	 * Gets the snapshot of the screen which this screen is accessed from. Used in design-time
	 * 
	 * @return snapshot of the accessed from screen
	 */
	TerminalSnapshot getAccessedFromSnapshot();

	/**
	 * Gets the screen size (rows, columns)
	 * 
	 * @return the screen size
	 */
	ScreenSize getScreenSize();

	/**
	 * Gets the screen field definitions sorted by their position ascending (row, column)
	 * 
	 * @return the screen field definition sorted by position
	 */
	List<ScreenFieldDefinition> getSortedFields();

	List<ScreenEntityBinder> getBinders();

	boolean isPerformDefaultBinding();
}
