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

import org.openlegacy.Session;
import org.openlegacy.SessionAction;

/**
 * An action definition. Translated from @ScreenAction and store within a screen entity in the registry
 * 
 */
public interface ActionDefinition {

	SessionAction<? extends Session> getAction();

	String getActionName();

	String getDisplayName();

	/**
	 * Allow to refer an action by a logical name, and not "technical" action name ("F3", etc)
	 */
	String getAlias();

}
