package org.openlegacy.terminal.support;

import apps.inventory.screens.MainMenu;
import apps.inventory.screens.SignOn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.exceptions.TerminalActionException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSessionTest extends AbstractTest {

	@Test
	public void testConnection() {
		TerminalSession terminalSession = newTerminalSession();

		Assert.assertFalse(terminalSession.isConnected());

		terminalSession.getSnapshot();

		Assert.assertTrue(terminalSession.isConnected());

		terminalSession.disconnect();

		Assert.assertFalse(terminalSession.isConnected());
	}

	@Test
	public void testCursor() {
		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertEquals("user", signOn.getFocusField());

		signOn.setUser("someuser");
		signOn.setPassword("somepwd");
		signOn.setFocusField("user");
		try {
			terminalSession.doAction(TerminalActions.ENTER(), signOn, MainMenu.class);
		} catch (SessionEndedException e) {
			// ok
		}
	}

	@Test(expected = TerminalActionException.class)
	public void testCursorException() {
		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);

		signOn.setUser("someuser");
		signOn.setPassword("somepwd");
		// cursor is expected to be at "user" at Sign-out.xml
		signOn.setFocusField("password");
		try {
			terminalSession.doAction(TerminalActions.ENTER(), signOn, MainMenu.class);
		} catch (SessionEndedException e) {
			// ok
		}
	}

	@Test(expected = TerminalActionException.class)
	public void testNotAllFieldsSent() {
		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);

		signOn.setUser("someuser");
		try {
			terminalSession.doAction(TerminalActions.ENTER(), signOn, MainMenu.class);
		} catch (SessionEndedException e) {
			// ok
		}
	}

	@Test(expected = TerminalActionException.class)
	public void testCursorIncorrect() {
		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		signOn.setFocusField("no_such_field");
		terminalSession.doAction(TerminalActions.ENTER(), signOn, MainMenu.class);
	}

	@Test
	public void testRowPart() {
		TerminalSession terminalSession = newTerminalSession();

		TerminalSnapshot snapshot = terminalSession.getSnapshot();
		TerminalRow row = snapshot.getRow(6);
		List<RowPart> rowParts = row.getRowParts();
		Assert.assertEquals(3, rowParts.size());
		Assert.assertEquals("                User  . . . . . . . . . . . . . .  ", rowParts.get(0).getValue());
		Assert.assertTrue(rowParts.get(1).isEditable());
		Assert.assertEquals(10, rowParts.get(1).getLength());
	}
}
