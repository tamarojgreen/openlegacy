package org.openlegacy.terminal.support.binders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.CustomBindScreen;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration("CustomBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomBinderTest extends AbstractTest {

	@Test
	public void testCustomBinder() {
		TerminalSession terminalSession = newTerminalSession();

		CustomBindScreen customBindScreen = terminalSession.getEntity(CustomBindScreen.class);
		Assert.assertEquals("AABB", customBindScreen.getFieldAandB());
		customBindScreen.setFieldAandB("A*B*");
		try {
			terminalSession.doAction(TerminalActions.ENTER(), customBindScreen);
		} catch (SessionEndedException e) {
			// good!
		}

	}

}
