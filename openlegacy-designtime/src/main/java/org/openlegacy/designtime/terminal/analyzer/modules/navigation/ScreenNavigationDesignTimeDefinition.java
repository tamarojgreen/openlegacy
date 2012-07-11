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
package org.openlegacy.designtime.terminal.analyzer.modules.navigation;

import org.openlegacy.terminal.definitions.SimpleScreenNavigationDefinition;

public class ScreenNavigationDesignTimeDefinition extends SimpleScreenNavigationDefinition {

	private String accessedFromEntityName;

	public void setAccessedFromEntityName(String accessedFromEntityName) {
		this.accessedFromEntityName = accessedFromEntityName;
	}

	public String getAccessedFromEntityName() {
		return accessedFromEntityName;
	}
}
