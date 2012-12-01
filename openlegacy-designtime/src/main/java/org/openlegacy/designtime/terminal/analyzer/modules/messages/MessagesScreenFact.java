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
package org.openlegacy.designtime.terminal.analyzer.modules.messages;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public class MessagesScreenFact implements ScreenFact {

	private List<TerminalField> messageFields;

	public MessagesScreenFact(List<TerminalField> messageFields) {
		this.messageFields = messageFields;
	}

	public List<TerminalField> getMessageFields() {
		return messageFields;
	}
}
