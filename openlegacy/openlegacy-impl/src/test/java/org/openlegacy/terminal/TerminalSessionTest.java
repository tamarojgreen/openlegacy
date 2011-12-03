package org.openlegacy.terminal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration(locations = "/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TerminalSessionTest extends AbstractTest {

	@Test
	public void testScreenContent() throws IOException {

		String signOnText = readResource("/apps/inventory/screens_text/SignOn.txt");
		Assert.assertEquals(signOnText, terminalSession.getSnapshot().toString());
		terminalSession.doAction(TerminalActions.ENTER());
		String displayProgramMessagesText = readResource("/apps/inventory/screens_text/MainMenu.txt");
		Assert.assertEquals(displayProgramMessagesText, terminalSession.getSnapshot().toString());
	}

}
