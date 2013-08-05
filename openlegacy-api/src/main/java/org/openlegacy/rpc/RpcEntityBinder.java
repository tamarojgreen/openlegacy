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

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;

/**
 * Defines a binder between a screen entity instance from a {@link RpcResult} and to a {@link RpcInvokeAction}, typically using
 * field mappings defined using {@link RpcField} annotations.
 * 
 * @author Roi Mor
 * 
 */
public interface RpcEntityBinder {

	/**
	 * populates the given entity with values from the given result. Typically using metadata on {@link EntityDefinition} from the
	 * {@link EntitiesRegistry}
	 * 
	 * @param entity
	 * @param result
	 */
	void populateEntity(Object entity, RpcResult result);

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
	void populateAction(RpcInvokeAction remoteAction, Object entity);
}