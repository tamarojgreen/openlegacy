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
package org.openlegacy.terminal.actions;

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.TerminalActionNotMappedException;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Represent a terminal action which is combined of a keyboard action and additional key (SHIFT, CTRL, ALT or NONE).
 * 
 * @author Roi Mor
 * 
 */
public class CombinedTerminalAction implements TerminalAction, Serializable {

	private static final long serialVersionUID = 1L;

	private AdditionalKey additionalKey;
	private Class<? extends TerminalAction> terminalAction;

	public void perform(TerminalSession session, Object entity) {
		// if we got here it means the actions is not mapped...
		throw (new TerminalActionNotMappedException(MessageFormat.format(
				"Specified action {0} is not mapped to a terminal command", getClass())));
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}

	public Class<? extends TerminalAction> getTerminalAction() {
		return terminalAction;
	}

	public void setTerminalAction(Class<? extends TerminalAction> terminalAction) {
		this.terminalAction = terminalAction;
	}

	@Override
	public int hashCode() {
		return getAdditionalKey().hashCode() + getTerminalAction().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CombinedTerminalAction)) {
			return false;
		}
		CombinedTerminalAction other = (CombinedTerminalAction)obj;
		return other.getAdditionalKey().equals(additionalKey) & other.getTerminalAction().equals(getTerminalAction());
	}
}
