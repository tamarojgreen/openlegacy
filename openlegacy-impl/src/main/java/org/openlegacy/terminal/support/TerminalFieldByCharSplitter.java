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

import java.util.ArrayList;
import java.util.List;

/**
 * Split field by a given text. Not allowing regular expression to avoid performance issues
 * 
 */
public class TerminalFieldByCharSplitter implements TerminalFieldSplitter {

	// default split to 2 blanks
	private String ch = " ";
	private int charCount = 2;

	public List<TerminalField> split(TerminalField terminalField, ScreenSize screenSize) {
		if (!terminalField.getValue().contains(ch)) {
			return null;
		}

		if (terminalField.getVisualValue() != null) {
			return null;
		}

		if (terminalField.isEditable()) {
			return null;
		}

		String value = terminalField.getValue();

		char theChar = ch.charAt(0);
		char[] chars = value.toCharArray();

		StringBuilder buffer = new StringBuilder();
		int hitCount = 0;

		List<TerminalField> terminalFields = new ArrayList<TerminalField>();

		int fieldStartOffset = 0;
		for (int i = 0; i < chars.length; i++) {

			char c = chars[i];

			if (c != theChar) {
				hitCount = 0;
				buffer.append(c);
			} else {
				hitCount++;
				if (hitCount >= charCount) {
					while (c == theChar) {
						buffer.append(c);
						i++;
						if (i >= chars.length) {
							break;
						}
						c = chars[i];
					}
					FieldSplitterUtil.addSplittedField(terminalField, buffer.toString(), terminalFields, fieldStartOffset,
							screenSize);
					buffer.setLength(0);
					fieldStartOffset = i;

					buffer.append(c);
				} else {
					buffer.append(c);
				}
			}
		}
		// handle the last one
		if (buffer.length() > 0) {
			FieldSplitterUtil.addSplittedField(terminalField, buffer.toString(), terminalFields, fieldStartOffset, screenSize);
		}

		return terminalFields;
	}

	public void setChar(String ch) {
		this.ch = ch;
	}

	public void setCharCount(int charCount) {
		this.charCount = charCount;
	}
}
