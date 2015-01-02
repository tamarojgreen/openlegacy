/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.db.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.support.SimpleActionDefinition;

import java.io.Serializable;

public class SimpleDbActionDefinition extends SimpleActionDefinition implements DbActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	public SimpleDbActionDefinition(SessionAction<? extends Session> action, String displayName) {
		super(action, displayName);
	}

	public SimpleDbActionDefinition(String actionName, String displayName) {
		super(actionName, displayName);
	}

}
