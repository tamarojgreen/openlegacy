package org.openlegacy.terminal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration("/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockTerminalConnectionTest extends AbstractTest {

	@Test
	public void testMockConnection() throws IOException {

		TerminalSession terminalSession = newTerminalSession();

		terminalSession.doAction(TerminalActions.ENTER());
	}

}
