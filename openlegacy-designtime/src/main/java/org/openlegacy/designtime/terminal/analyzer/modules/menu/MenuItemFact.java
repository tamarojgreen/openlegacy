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
package org.openlegacy.designtime.terminal.analyzer.modules.menu;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

public class MenuItemFact {

	private TerminalField codeField;
	private TerminalField captionField;
	private ScreenEntityDefinition screenEntityDefinition;

	public MenuItemFact(TerminalField codeField, TerminalField captionField, ScreenEntityDefinition screenEntityDefinition) {
		this.codeField = codeField;
		this.captionField = captionField;
		this.screenEntityDefinition = screenEntityDefinition;
	}

	public TerminalField getCodeField() {
		return codeField;
	}

	public TerminalField getCaptionField() {
		return captionField;
	}

	public ScreenEntityDefinition getScreenEntityDefinition() {
		return screenEntityDefinition;
	}
}
