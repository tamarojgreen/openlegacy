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
package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.support.fact_processors.AbstractActionFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.fact_processors.ScreenActionFact;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.SimpleTerminalActionDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions;
import org.openlegacy.terminal.support.TerminalPositionContainerComparator;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public class ScreenTableActionFactProcessor extends AbstractActionFactProcessor {

	private final static Log logger = LogFactory.getLog(ScreenTableActionFactProcessor.class);

	@Override
	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof ScreenTableActionFact;
	}

	@Override
	protected TerminalActionDefinition buildActionDefinition(String actionValue, String caption, TerminalPosition terminalPosition) {
		TerminalDrilldownAction drilldownAction = TerminalDrilldownActions.enter(actionValue);
		TerminalActionDefinition actionDefinition = new SimpleTerminalActionDefinition(drilldownAction, AdditionalKey.NONE,
				caption, terminalPosition);
		return actionDefinition;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void addAction(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenActionFact screenActionFact,
			TerminalActionDefinition actionDefinition) {
		ScreenTableActionFact screenTableActionFact = (ScreenTableActionFact)screenActionFact;

		ScreenTableDefinition tableDefinition = screenTableActionFact.getScreenTableDefinition();
		List actions = tableDefinition.getActions();

		actions.add(actionDefinition);

		Collections.sort(actions, TerminalPositionContainerComparator.instance());

		logger.info(MessageFormat.format("Added action {0}:{1} to table entity",
				actionDefinition.getAction().getClass().getName(), actionDefinition.getDisplayName()));

	}

}
