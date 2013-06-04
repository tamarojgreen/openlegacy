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

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.annotations.screen.Action;

/**
 * A generic session action meta-data definition. Stored within {@link EntityDefinition} in {@link EntitiesRegistry}. <br/>
 * Loaded from {@link Action} annotation for screen entities.
 * 
 * @author Roi Mor
 * 
 */
public interface ActionDefinition {

	/**
	 * The session action to execute
	 * 
	 * @return the session action to execute
	 */
	SessionAction<? extends Session> getAction();

	/**
	 * the action name
	 * 
	 * @return the action name
	 */
	String getActionName();

	/**
	 * The action display name
	 * 
	 * @return the action display name
	 */
	String getDisplayName();

	/**
	 * Allow to refer an action by a logical name
	 */
	String getAlias();

	/**
	 * Whether the action is the default action from a given list of actions
	 * 
	 * @return Whether the action is the default action
	 */
	boolean isDefaultAction();

	/**
	 * Whether the action is global or not
	 * 
	 * @return whether the action is global
	 */
	boolean isGlobal();

	Class<?> getTargetEntity();
}
