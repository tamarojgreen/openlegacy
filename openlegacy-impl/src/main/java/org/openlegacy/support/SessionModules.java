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
package org.openlegacy.support;

import org.openlegacy.modules.SessionModule;

import java.io.Serializable;
import java.util.List;

/**
 * Container class for
 * 
 */
public class SessionModules implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<? extends SessionModule> modules;

	public List<? extends SessionModule> getModules() {
		return modules;
	}

	public void setModules(List<? extends SessionModule> modules) {
		this.modules = modules;
	}
}
