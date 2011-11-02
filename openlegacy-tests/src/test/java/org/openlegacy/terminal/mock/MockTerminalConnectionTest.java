package org.openlegacy.terminal.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MockTerminalConnectionTest extends AbstractTest {

	@Autowired
	TerminalSession terminalSession;

	@Test
	public void testMockConnection() throws IOException {

		terminalSession.doAction(SendKeyActions.ENTER, null);
	}

}
