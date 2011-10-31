package org.openlegacy.terminal.modules.login;

import com.someorg.examples.screens.SignOn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.login.LoginModule;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailModule;
import org.openlegacy.modules.trail.TrailStage;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.trail.TerminalOutgoingTrailStage;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginModuleImplTest extends AbstractTest {

	@Autowired
	private TerminalSession terminalSession;

	@Test
	public void testLoginObject() {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		signOn.setUser("someuser");
		signOn.setPassword("somepwd");
		terminalSession.getModule(LoginModule.class).login(signOn);

		assertLoginPeformed();
	}

	@Test
	public void testLoginSimple() {
		terminalSession.getModule(LoginModule.class).login("someuser", "somepwd");

		assertLoginPeformed();
	}

	private void assertLoginPeformed() {
		SessionTrail trail = terminalSession.getModule(TrailModule.class).getSessionTrail();
		Assert.assertEquals(2, trail.getStages().size());
		TrailStage loginStage = trail.getStages().get(0);
		Assert.assertEquals(loginStage.getClass(), TerminalOutgoingTrailStage.class);

		Map<ScreenPosition, String> fields = ((TerminalOutgoingTrailStage)loginStage).getTerminalSendAction().getFields();
		Set<ScreenPosition> fieldPositions = fields.keySet();
		Iterator<ScreenPosition> iterator = fieldPositions.iterator();

		ScreenPosition firstPosition = iterator.next();
		Assert.assertEquals(new SimpleScreenPosition(6, 53), firstPosition);
		Assert.assertEquals("someuser", fields.get(firstPosition));

		ScreenPosition secondPosition = iterator.next();
		Assert.assertEquals(new SimpleScreenPosition(7, 53), secondPosition);
		Assert.assertEquals("somepwd", fields.get(secondPosition));

		Assert.assertTrue(terminalSession.getModule(LoginModule.class).isLoggedIn());
	}
}
