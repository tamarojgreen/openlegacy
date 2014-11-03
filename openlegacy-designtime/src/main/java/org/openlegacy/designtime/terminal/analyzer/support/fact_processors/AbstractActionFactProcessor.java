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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.TerminalActionAnalyzer;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import javax.inject.Inject;

public abstract class AbstractActionFactProcessor implements ScreenFactProcessor {

	@Inject
	private TerminalActionAnalyzer terminalActionAnalyzer;

	@Override
	public boolean accept(ScreenFact screenFact) {
		return screenFact.getClass() == ScreenActionFact.class;
	}

	@Override
	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ScreenActionFact screenActionFact = (ScreenActionFact)screenFact;

		String caption = screenActionFact.getCaption().replaceAll("\"", "\\\\\"");
		TerminalActionDefinition actionDefinition = buildActionDefinition(screenActionFact.getAction(), caption,
				screenActionFact.getTerminalPosition());

		if (actionDefinition != null) {
			addAction(screenEntityDefinition, screenActionFact, actionDefinition);
		}

	}

	protected abstract TerminalActionDefinition buildActionDefinition(String action, String caption,
			TerminalPosition terminalPosition);

	protected abstract void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenActionFact screenActionFact,
			TerminalActionDefinition actionDefinition);

	protected TerminalActionAnalyzer getTerminalActionAnalyzer() {
		return terminalActionAnalyzer;
	}
}
