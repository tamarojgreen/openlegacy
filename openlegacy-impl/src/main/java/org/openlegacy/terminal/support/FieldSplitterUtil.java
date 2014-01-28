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
import org.openlegacy.utils.StringUtil;

import java.util.List;

public class FieldSplitterUtil {

	public static void addSplittedField(TerminalField terminalField, String text, List<TerminalField> terminalFields,
			int fieldStartOffset, ScreenSize screenSize) {
		ModifiableTerminalField newTerminalField = (ModifiableTerminalField)terminalField.clone();
		// don't add empty fields
		if (StringUtil.isEmpty(text)) {
			return;
		}
		newTerminalField.setValue(text, false);
		newTerminalField.setPosition(SnapshotUtils.moveBy(newTerminalField.getPosition(), fieldStartOffset, screenSize));
		newTerminalField.setEndPosition(newTerminalField.getPosition().moveBy(text.length() - 1));
		newTerminalField.setLength(text.length());
		terminalFields.add(newTerminalField);
	}
}
