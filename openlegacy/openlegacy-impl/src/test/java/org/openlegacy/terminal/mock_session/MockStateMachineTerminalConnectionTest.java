package org.openlegacy.terminal.mock_session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.mock_session.mock.Login;
import org.openlegacy.terminal.mock_session.mock.MenuScreen;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MockStateMachineTerminalConnectionTest extends AbstractTest {

	@Test
	public void testMockConnection() throws IOException {

		TerminalSession terminalSession = newTerminalSession();

		Login login = terminalSession.getEntity(Login.class);
		login.setUser("someuser");
		login.setPassword("goodpwd");
		// should know to skip bad login in the snapshots
		MenuScreen menu = terminalSession.doAction(TerminalActions.ENTER(), login, MenuScreen.class);
		Assert.assertNotNull(menu);

		login = terminalSession.doAction(TerminalActions.F3(), null, Login.class);
		Assert.assertNotNull(login);
	}
}
