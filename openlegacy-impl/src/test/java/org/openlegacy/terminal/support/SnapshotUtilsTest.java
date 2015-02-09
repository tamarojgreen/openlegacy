package org.openlegacy.terminal.support;

import org.junit.Test;
import org.openlegacy.terminal.TerminalPosition;

import junit.framework.Assert;

public class SnapshotUtilsTest {

	@Test
	public void testMoveBySameLine() {
		TerminalPosition endPosition = SnapshotUtils.moveBy(new SimpleTerminalPosition(18, 70), 5, SimpleScreenSize.DEFAULT);
		Assert.assertEquals(18, endPosition.getRow());
		Assert.assertEquals(75, endPosition.getColumn());

	}

	@Test
	public void testMoveByNewLine() {
		TerminalPosition endPosition = SnapshotUtils.moveBy(new SimpleTerminalPosition(18, 70), 20, SimpleScreenSize.DEFAULT);
		Assert.assertEquals(19, endPosition.getRow());
		Assert.assertEquals(10, endPosition.getColumn());
	}

	@Test(expected = IllegalStateException.class)
	public void testMoveByToMuch() {
		SnapshotUtils.moveBy(new SimpleTerminalPosition(24, 70), 20, SimpleScreenSize.DEFAULT);
	}
}
