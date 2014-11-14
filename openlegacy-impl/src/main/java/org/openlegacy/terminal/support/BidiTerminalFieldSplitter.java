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
import org.openlegacy.utils.BidiUtil;
import org.springframework.util.Assert;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;

public class BidiTerminalFieldSplitter implements TerminalFieldSplitter {

	@Override
	public List<TerminalField> split(TerminalField terminalField, ScreenSize screenSize) {
		Assert.notNull(terminalField);
		String visualValue = terminalField.getVisualValue();
		if (visualValue == null) {
			return null;
		}

		Bidi bidi = new Bidi(terminalField.getVisualValue(), Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
		int runs = bidi.getRunCount();
		List<TerminalField> fields = new ArrayList<TerminalField>();
		for (int i = 0; i < runs; i++) {
			int index = bidi.getRunStart(i);
			int endIndex = bidi.getRunLimit(i);
			String part = visualValue.substring(index, endIndex);
			part = BidiUtil.convertToLogical(part, false);
			FieldSplitterUtil.addSplittedField(terminalField, part, fields, index, screenSize);
		}
		return fields;
	}
}
