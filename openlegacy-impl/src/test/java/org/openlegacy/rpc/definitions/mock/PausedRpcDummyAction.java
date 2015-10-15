/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.rpc.definitions.mock;

import org.openlegacy.annotations.entity.Paused;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;

@Paused
@RpcEntity(displayName = "Dummy Entity")
public class PausedRpcDummyAction {

	@RpcField(direction = Direction.INPUT, length = 20, key = true)
	String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
