package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.terminal.analyzer.BestEntityNameFieldComparator;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;

public class ByMostCenteredFieldComparator implements BestEntityNameFieldComparator {

	public int compare(TerminalField field1, TerminalField field2) {

		int field1DistanceFromCenter = Math.abs((ScreenSize.DEFAULT_COLUMN / 2) - field1.getPosition().getColumn());
		int field2DistanceFromCenter = Math.abs((ScreenSize.DEFAULT_COLUMN / 2) - field2.getPosition().getColumn());

		// the field who is distance is smallest should be 1st
		return field1DistanceFromCenter - field2DistanceFromCenter;
	}
}
