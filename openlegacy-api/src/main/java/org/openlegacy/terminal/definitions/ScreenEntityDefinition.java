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
import org.openlegacy.definitions.ActionDefinition;
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
	 * @return
	 */
	Map<String, ScreenTableDefinition> getTableDefinitions();

	/**
	 * field name -> part definition
	 * 
	 * @return
	 */
	Map<String, ScreenPartEntityDefinition> getPartsDefinitions();

	List<ActionDefinition> getActions();

	TerminalSnapshot getSnapshot();

	/**
	 * Holds a snapshot with analyzer manipulations
	 */
	TerminalSnapshot getOriginalSnapshot();

	boolean isWindow();

	boolean isChild();

	ScreenEntityDefinition getAccessedFromScreenDefinition();

	TerminalSnapshot getAccessedFromSnapshot();

	ScreenSize getScreenSize();

	List<ScreenFieldDefinition> getSortedFields();

}
