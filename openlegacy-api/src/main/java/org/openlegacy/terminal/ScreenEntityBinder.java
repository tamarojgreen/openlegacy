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
package org.openlegacy.terminal;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.annotations.screen.ScreenField;

/**
 * Defines a binder between a screen entity instance from a {@link TerminalSnapshot} and to a {@link TerminalSendAction},
 * typically using field mappings defined using {@link ScreenField} annotations.
 * 
 * @author Roi Mor
 * 
 */
public interface ScreenEntityBinder {

	/**
	 * populates the given entity with values from the given snapshot. Typically using metadata on {@link EntityDefinition} from
	 * the {@link EntitiesRegistry}
	 * 
	 * @param entity
	 * @param snapshot
	 */
	void populateEntity(Object entity, TerminalSnapshot snapshot);

	/**
	 * Populates the given send action with values from the entity
	 * 
	 * @param remoteAction
	 *            The send action to populate
	 * @param snapshot
	 *            used for comparing current values of fields to new values from the entity
	 * @param entity
	 *            the entity from which new values are taken
	 */
	void populateAction(TerminalSendAction remoteAction, TerminalSnapshot snapshot, Object entity);

}
