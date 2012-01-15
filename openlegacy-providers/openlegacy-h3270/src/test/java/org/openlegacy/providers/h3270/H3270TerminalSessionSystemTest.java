package org.openlegacy.providers.h3270;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/test-h3270-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class H3270TerminalSessionSystemTest extends AbstractTest {

	@Test
	public void testH3270System() throws Exception {
		TerminalSession terminalSession = newTerminalSession();
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
	}

}
