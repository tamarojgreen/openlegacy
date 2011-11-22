package org.openlegacy.terminal.modules.login;

import apps.inventory.screens.ItemsList;
import apps.inventory.screens.SignOn;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultLoginModuleTest extends AbstractTest {

	@Inject
	private ScreensRecognizer screensRecognizer;

	@After
	public void logoff() {
		terminalSession.getModule(Login.class).logoff();
	}

	@Test
	public void testLoginObject() {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		signOn.setUser("someuser");
		signOn.setPassword("somepwd");
		terminalSession.getModule(Login.class).login(signOn);

	}

	@Test
	public void testLoginSimple() {
		terminalSession.getModule(Login.class).login("someuser", "somepwd");
	}

	@Test
	public void testLogoff() {
		terminalSession.getModule(Login.class).login("someuser", "somepwd");

		Assert.assertTrue(terminalSession.isConnected());
		Assert.assertTrue(terminalSession.getModule(Login.class).isLoggedIn());

		ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(itemsList);

		terminalSession.getModule(Login.class).logoff();

		Assert.assertFalse(terminalSession.getModule(Login.class).isLoggedIn());
		Assert.assertFalse(terminalSession.isConnected());

		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		Snapshot lastSnapshot = trail.getSnapshots().get(trail.getSnapshots().size() - 1); // get the last snapshot

		Class<?> lastSnapshotClass = screensRecognizer.match((TerminalSnapshot)lastSnapshot);

		Assert.assertEquals(lastSnapshotClass, SignOn.class);

	}

}
