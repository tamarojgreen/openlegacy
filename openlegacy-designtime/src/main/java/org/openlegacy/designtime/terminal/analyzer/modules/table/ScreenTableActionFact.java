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
package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.openlegacy.designtime.terminal.analyzer.support.fact_processors.ScreenActionFact;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;

public class ScreenTableActionFact extends ScreenActionFact {

	private ScreenTableDefinition screenTableDefinition;

	public ScreenTableActionFact(ScreenTableDefinition screenTableDefinition, String captionAction,
			TerminalPosition terminalPosition, String trueFalseRegex) {
		super(captionAction, terminalPosition, trueFalseRegex);
		this.screenTableDefinition = screenTableDefinition;
	}

	public ScreenTableDefinition getScreenTableDefinition() {
		return screenTableDefinition;
	}

}
