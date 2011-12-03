package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenPositionContainer;
import org.openlegacy.terminal.support.SnapshotUtils;

import java.util.Comparator;

public class ScreenPositionContainerComparator implements Comparator<ScreenPositionContainer> {

	private static ScreenPositionContainerComparator instance = new ScreenPositionContainerComparator();

	public static ScreenPositionContainerComparator instance() {
		return instance;
	}

	public int compare(ScreenPositionContainer o1, ScreenPositionContainer o2) {
		ScreenPosition position1 = o1.getPosition();
		ScreenPosition position2 = o2.getPosition();

		return SnapshotUtils.comparePositions(position1, position2);
	}

}
