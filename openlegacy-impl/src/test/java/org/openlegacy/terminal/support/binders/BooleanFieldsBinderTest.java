package org.openlegacy.terminal.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.BooleanScreen;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class BooleanFieldsBinderTest extends AbstractTest {

	@Test
	public void testFillBooleanField() {
		TerminalSession terminalSession = newTerminalSession();

		BooleanScreen booleanScreen = terminalSession.getEntity(BooleanScreen.class);
		Assert.assertTrue(booleanScreen.getBooleanTrue());
		Assert.assertFalse(booleanScreen.getBooleanFalse());
		Assert.assertFalse(booleanScreen.getBooleanEmpty());

	}

	@Test
	public void testSendBooleanField() {
		TerminalSession terminalSession = newTerminalSession();

		BooleanScreen booleanScreen = terminalSession.getEntity(BooleanScreen.class);
		booleanScreen.setBooleanTrue(true);

		try {
			terminalSession.doAction(TerminalActions.ENTER(), booleanScreen);
		} catch (SessionEndedException e) {
			// OK
		}

	}
}
