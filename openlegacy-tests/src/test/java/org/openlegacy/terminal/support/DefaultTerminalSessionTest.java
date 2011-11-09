package org.openlegacy.terminal.support;

import apps.inventory.screens.MainMenu;
import apps.inventory.screens.SignOn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.openlegacy.terminal.exceptions.SendActionException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSessionTest extends AbstractTest {

	@Test
	public void testConnection() {
		Assert.assertFalse(terminalSession.isConnected());

		terminalSession.getSnapshot();

		Assert.assertTrue(terminalSession.isConnected());

		terminalSession.disconnect();

		Assert.assertFalse(terminalSession.isConnected());
	}

	@Test
	public void testCursor() {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		signOn.setUser("someuser");
		signOn.setFocusField("user");
		terminalSession.doAction(SendKeyActions.ENTER, signOn, MainMenu.class);

		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();
		TerminalOutgoingSnapshot snapshot = (TerminalOutgoingSnapshot)trail.getSnapshots().get(0);
		ScreenPosition cursorPosition = snapshot.getTerminalSendAction().getCursorPosition();
		Assert.assertEquals(new SimpleScreenPosition(6, 53), cursorPosition);
	}

	@Test(expected = SendActionException.class)
	public void testCursorIncorrect() {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		signOn.setFocusField("no_such_field");
		terminalSession.doAction(SendKeyActions.ENTER, signOn, MainMenu.class);
	}
}
