package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.utils.StringUtil;

public class ByCharsFrequencyFieldComparator implements BestEntityNameFieldComparator {

	public int compare(TerminalField field1, TerminalField field2) {
		int field1CharsFrquency = StringUtil.getDifferentCharsCount(field1.getValue());
		int field2CharsFrquency = StringUtil.getDifferentCharsCount(field2.getValue());

		return field2CharsFrquency - field1CharsFrquency;
	}
}
