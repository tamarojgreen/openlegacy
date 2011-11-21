package org.openlegacy.terminal.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TerminalEqualsHashcodeTest extends AbstractTest {

	@Test
	public void testSnapshotsEquals() {
		TerminalScreen snapshot1 = terminalSession.getSnapshot();
		terminalSession.doAction(TerminalActions.ENTER());
		TerminalScreen snapshot2 = terminalSession.getSnapshot();

		TerminalEqualsHashcodeUtil.snapshotsEquals(snapshot1, snapshot2);
	}
}
