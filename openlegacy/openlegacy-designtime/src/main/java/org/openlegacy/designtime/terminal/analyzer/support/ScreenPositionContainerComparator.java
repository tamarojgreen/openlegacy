package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenPositionContainer;

import java.util.Comparator;

public class ScreenPositionContainerComparator implements Comparator<ScreenPositionContainer> {

	private static ScreenPositionContainerComparator instance = new ScreenPositionContainerComparator();

	public static ScreenPositionContainerComparator instance() {
		return instance;
	}

	public int compare(ScreenPositionContainer o1, ScreenPositionContainer o2) {
		ScreenPosition position1 = o1.getPosition();
		ScreenPosition position2 = o2.getPosition();

		if (position1.getRow() != position2.getRow()) {
			return position1.getRow() - position2.getRow();
		}
		return position1.getColumn() - position2.getColumn();
	}

}
