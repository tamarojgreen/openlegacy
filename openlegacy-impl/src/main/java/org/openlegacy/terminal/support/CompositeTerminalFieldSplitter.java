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
package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldSplitter;

import java.util.List;

public class CompositeTerminalFieldSplitter implements TerminalFieldSplitter {

	private List<TerminalFieldSplitter> terminalFieldSplitters;

	@Override
	public List<TerminalField> split(TerminalField terminalField, ScreenSize screenSize) {
		for (TerminalFieldSplitter terminalFieldSplitter : terminalFieldSplitters) {
			List<TerminalField> splittedFields = terminalFieldSplitter.split(terminalField, screenSize);
			if (splittedFields != null) {
				return splittedFields;
			}
		}
		return null;
	}

	public void setTerminalFieldSplitters(List<TerminalFieldSplitter> terminalFieldSplitters) {
		this.terminalFieldSplitters = terminalFieldSplitters;
	}
}
