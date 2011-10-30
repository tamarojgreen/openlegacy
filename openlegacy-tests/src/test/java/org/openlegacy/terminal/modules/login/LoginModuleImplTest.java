package org.openlegacy.terminal.modules.login;

import com.someorg.examples.screens.SignOn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginModuleImplTest extends AbstractTest {

	@Autowired
	private TerminalSession terminalSession;

	@Test
	public void testLogin() {
		SignOn signOn = new SignOn();
		signOn.setUser("TestUser");
		signOn.setPassword("TestPwd");
		terminalSession.getModule(Login.class).login(signOn);

		Assert.assertTrue(terminalSession.getModule(Login.class).isLoggedIn());
	}
}
