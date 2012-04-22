package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.utils.StringUtil;

public class ByPotentialClassNameLengthFieldComparator implements BestEntityNameFieldComparator {

	public int compare(TerminalField field1, TerminalField field2) {
		String field1Variable = StringUtil.toClassName(field1.getValue());
		String field2Variable = StringUtil.toClassName(field2.getValue());

		return field2Variable.length() - field1Variable.length();
	}
}
