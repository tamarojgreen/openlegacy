package org.openlegacy.terminal.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.MultyLineFieldScreen;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration("MultyLineFieldTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MultyLineFieldTest extends AbstractTest {

	@Test
	public void testCustomBinder() {
		TerminalSession terminalSession = newTerminalSession();

		MultyLineFieldScreen multyLineFieldScreen = terminalSession.getEntity(MultyLineFieldScreen.class);
		Assert.assertEquals("AA BBCC DD", multyLineFieldScreen.getMultlyLineField());

		Assert.assertEquals("This field continue in next line", multyLineFieldScreen.getMultlyLineBreakingField());

		// validated during the send
		multyLineFieldScreen.setMultlyLineField("A$ B$C$ D$");
		multyLineFieldScreen.setMultlyLineBreakingField("This field was modified");

		try {
			terminalSession.doAction(TerminalActions.ENTER(), multyLineFieldScreen);
		} catch (SessionEndedException e) {
			// OK
		}

	}
}
