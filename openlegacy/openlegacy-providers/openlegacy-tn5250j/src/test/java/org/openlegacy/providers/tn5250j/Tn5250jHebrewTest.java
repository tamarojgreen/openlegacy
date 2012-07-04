package org.openlegacy.providers.tn5250j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/test-tn5250j-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Tn5250jHebrewTest extends AbstractTest {

	@Test
	public void testHebrew() throws Exception {
		TerminalSession terminalSession = newTerminalSession();
		System.out.println(terminalSession.getSnapshot());
		terminalSession.doAction(TerminalActions.ENTER());
		System.out.println(terminalSession.getSnapshot());
	}

}
