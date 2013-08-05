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

import org.openlegacy.RemoteAction;

/**
 * Defines a low level rpc send action on a {@link RpcConnection}. Contains rpc name and input fields.
 * 
 * @author Roi Mor
 * 
 */
public interface RpcInvokeAction extends RemoteAction<RpcField> {

	String getAction();

	String getRpcPath();
}