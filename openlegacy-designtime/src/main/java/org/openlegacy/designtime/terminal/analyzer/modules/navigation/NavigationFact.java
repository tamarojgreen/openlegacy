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

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshot;

public class NavigationFact implements ScreenFact {

	private ScreenEntityDesigntimeDefinition accessedFromScreenEntityDefinition;
	private TerminalSnapshot accessedFromSnapshot;

	public NavigationFact(ScreenEntityDesigntimeDefinition screenEntityDefinition, TerminalSnapshot accessedFromSnapshot) {
		this.accessedFromScreenEntityDefinition = screenEntityDefinition;
		this.accessedFromSnapshot = accessedFromSnapshot;
	}

	public ScreenEntityDesigntimeDefinition getAccessedFromScreenEntityDefinition() {
		return accessedFromScreenEntityDefinition;
	}

	public TerminalSnapshot getAccessedFromSnapshot() {
		return accessedFromSnapshot;
	}

}
