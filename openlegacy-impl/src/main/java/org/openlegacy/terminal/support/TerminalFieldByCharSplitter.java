package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldSplitter;
import org.openlegacy.utils.StringUtil;

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

	public List<TerminalField> split(TerminalField terminalField) {
		if (!terminalField.getValue().contains(ch)) {
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
					addSplittedField(terminalField, buffer, terminalFields, fieldStartOffset);
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
			addSplittedField(terminalField, buffer, terminalFields, fieldStartOffset);
		}

		return terminalFields;
	}

	private static void addSplittedField(TerminalField terminalField, StringBuilder buffer, List<TerminalField> terminalFields,
			int fieldStartOffset) {
		ModifiableTerminalField newTerminalField = (ModifiableTerminalField)terminalField.clone();
		String newText = buffer.toString();
		// don't add empty fields
		if (StringUtil.isEmpty(newText)) {
			return;
		}
		newTerminalField.setValue(newText, false);
		newTerminalField.setPosition(newTerminalField.getPosition().moveBy(fieldStartOffset));
		newTerminalField.setEndPosition(newTerminalField.getPosition().moveBy(newText.length() - 1));
		newTerminalField.setLength(newText.length());
		terminalFields.add(newTerminalField);
	}

	public void setChar(String ch) {
		this.ch = ch;
	}

	public void setCharCount(int charCount) {
		this.charCount = charCount;
	}
}
