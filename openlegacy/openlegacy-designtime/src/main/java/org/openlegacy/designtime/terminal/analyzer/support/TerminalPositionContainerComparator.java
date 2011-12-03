package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.support.SnapshotUtils;

import java.util.Comparator;

public class TerminalPositionContainerComparator implements Comparator<TerminalPositionContainer> {

	private static TerminalPositionContainerComparator instance = new TerminalPositionContainerComparator();

	public static TerminalPositionContainerComparator instance() {
		return instance;
	}

	public int compare(TerminalPositionContainer o1, TerminalPositionContainer o2) {
		TerminalPosition position1 = o1.getPosition();
		TerminalPosition position2 = o2.getPosition();

		return SnapshotUtils.comparePositions(position1, position2);
	}

}
