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
package org.openlegacy.rpc;

import org.openlegacy.EntityType;

/**
 * Screen entity type define the business purpose of the entity. Example usage: Login screen, menu screen, etc
 * 
 * ScreenEntityType may be used by session modules which are interested of understand the legacy application entities/screens, by
 * querying the registry
 * 
 * It is possible to define more screen entity types by implementing this interface
 * 
 * @author Roi Mor
 */
public interface RpcEntityType extends EntityType {

	public static class General implements RpcEntityType {
	}

	public static class MasterDetailsEntity implements RpcEntityType {
	}
}
