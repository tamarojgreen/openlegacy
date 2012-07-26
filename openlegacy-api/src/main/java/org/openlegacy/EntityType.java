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
package org.openlegacy;

import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.menu.Menu.MenuEntity;

/**
 * Entity type define the business purpose of the entity. Example usage: login screen, menu screen, etc
 * 
 * EntityType may be used by session modules which are interested of understand the legacy application entities/screens, by
 * querying the registry
 * 
 * There are out of the box entity types, and it's is possible to define more entity types by implementing this interface.
 * 
 * @see LoginEntity
 * @see MenuEntity
 * 
 * @author Roi Mor
 * 
 */
public interface EntityType {

}
