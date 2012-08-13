package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.utils.StringUtil;

import java.util.List;

public class FieldSplitterUtil {

	public static void addSplittedField(TerminalField terminalField, String text, List<TerminalField> terminalFields,
			int fieldStartOffset) {
		ModifiableTerminalField newTerminalField = (ModifiableTerminalField)terminalField.clone();
		// don't add empty fields
		if (StringUtil.isEmpty(text)) {
			return;
		}
		newTerminalField.setValue(text, false);
		newTerminalField.setPosition(newTerminalField.getPosition().moveBy(fieldStartOffset));
		newTerminalField.setEndPosition(newTerminalField.getPosition().moveBy(text.length() - 1));
		newTerminalField.setLength(text.length());
		terminalFields.add(newTerminalField);
	}
}
