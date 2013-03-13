package org.openlegacy.designtime.terminal.analyzer.support.comparators;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.TerminalField;

import java.util.List;

public class CompositeFieldsComparator implements BestEntityNameFieldComparator {

	private List<BestEntityNameFieldComparator> comparators;

	public int compare(TerminalField field1, TerminalField field2) {
		for (BestEntityNameFieldComparator comparator : comparators) {
			int result = comparator.compare(field1, field2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	public void setComparators(List<BestEntityNameFieldComparator> comparators) {
		this.comparators = comparators;
	}
}