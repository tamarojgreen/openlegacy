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
package org.openlegacy.rpc.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.definitions.support.SimpleActionDefinition;

import java.io.Serializable;

public class SimpleRpcActionDefinition extends SimpleActionDefinition implements RpcActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;
	private String programPath;

	public SimpleRpcActionDefinition(SessionAction<? extends Session> action, String displayName) {
		super(action, displayName);
	}

	public SimpleRpcActionDefinition(String actionName, String displayName) {
		super(actionName, displayName);
	}

	public String getProgramPath() {
		return programPath;
	}

	public void setPath(String programPath) {
		this.programPath = programPath;
	}

}
