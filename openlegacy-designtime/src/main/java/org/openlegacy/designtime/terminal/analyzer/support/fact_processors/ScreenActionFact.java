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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalPosition;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScreenActionFact implements ScreenFact {

	private TerminalPosition terminalPosition;
	private String caption;
	private String action;

	private final static Log logger = LogFactory.getLog(ScreenActionFact.class);

	public ScreenActionFact(String actionCaption, TerminalPosition terminalPosition, String regex) {
		this.terminalPosition = terminalPosition;

		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(actionCaption);

		match.find();
		if (match.groupCount() < 2) {
			logger.warn(MessageFormat.format("text is not in the format of: action -> displayName", actionCaption));
			return;
		}

		action = match.group(1);
		caption = match.group(2);

	}

	public ScreenActionFact(String caption, String action, TerminalPosition terminalPosition) {
		this.terminalPosition = terminalPosition;
		this.caption = caption;
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public String getCaption() {
		return caption;
	}

	public TerminalPosition getTerminalPosition() {
		return terminalPosition;
	}
}
