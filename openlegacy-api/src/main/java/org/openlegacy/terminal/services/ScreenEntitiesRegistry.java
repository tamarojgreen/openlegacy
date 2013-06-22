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
package org.openlegacy.terminal.services;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;

/**
 * Defines a registry for screen related entities registration. Stores all information gathers from screen entities classes and
 * their related annotations, into a registry which contains multiple {@link ScreenEntityDefinition},
 * {@link ScreenTableDefinition} and {@link ScreenPartEntityDefinition}
 * 
 * @author Roi Mor
 * 
 */
public interface ScreenEntitiesRegistry extends EntitiesRegistry<ScreenEntityDefinition, ScreenFieldDefinition, ScreenPartEntityDefinition> {

	void addTable(ScreenTableDefinition tableDefinition);

	ScreenTableDefinition getTable(Class<?> containingClass);

}
