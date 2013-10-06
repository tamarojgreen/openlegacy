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
package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldSplitter;
import org.openlegacy.terminal.TerminalFieldsSplitter;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SimpleTerminalFieldsSplitter implements TerminalFieldsSplitter {

	@Inject
	TerminalFieldSplitter terminalFieldSplitter;

	public List<TerminalField> splitFields(List<TerminalField> fields, ScreenSize screenSize) {
		List<TerminalField> logicalFields = new ArrayList<TerminalField>();
		for (TerminalField terminalField : fields) {
			List<TerminalField> splittedFields = terminalFieldSplitter.split(terminalField, screenSize);
			if (splittedFields != null) {
				logicalFields.addAll(splittedFields);
			} else {
				logicalFields.add(terminalField);
			}

		}
		return logicalFields;
	}

	public List<TerminalField> splitFields(TerminalSnapshot terminalSnapshot) {
		return splitFields(terminalSnapshot.getFields(), terminalSnapshot.getSize());
	}

}
