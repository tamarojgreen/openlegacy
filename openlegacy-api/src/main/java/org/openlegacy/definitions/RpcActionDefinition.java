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

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;

/**
 * A session action definition. Translated from {@link Action} and store within a {@link RpcEntityDefinition} into
 * {@link RpcEntitiesRegistry}
 * 
 * @author Roi Mor
 * 
 */
public interface RpcActionDefinition extends ActionDefinition {

	String getProgramPath();
}
