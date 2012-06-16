package org.openlegacy.terminal.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("TerminalEqualsHashcodeTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TerminalEqualsHashcodeTest extends AbstractTest {

	@Test
	public void testSnapshotsEquals() {
		TerminalSession terminalSession = newTerminalSession();

		TerminalSnapshot snapshot1 = terminalSession.getSnapshot();
		terminalSession.doAction(TerminalActions.ENTER());
		TerminalSnapshot snapshot2 = terminalSession.getSnapshot();

		TerminalEqualsHashcodeUtil.snapshotsEquals(snapshot1, snapshot2);
	}
}
