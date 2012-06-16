package org.openlegacy.terminal.modules.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("DefaultLoginModuleFailedTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultLoginModuleFailedTest extends AbstractTest {

	@Test(expected = LoginException.class)
	public void testLoginFailed() throws LoginException {
		TerminalSession terminalSession = newTerminalSession();

		terminalSession.getModule(Login.class).login("user", "pwd");
	}

}
