package org.openlegacy.terminal;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.adapter.terminal.SendKeyActions;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TerminalSessionTest extends AbstractTest {

	@Autowired
	private TerminalSession terminalSession;

	@Test
	public void testScreenContent() throws IOException {

		String signOnText = readResource("/screens/SignOn.txt");
		Assert.assertEquals(signOnText, terminalSession.getSnapshot()
				.toString());
		terminalSession.doAction(SendKeyActions.ENTER);
		String displayProgramMessagesText = readResource("/screens/MainMenu.txt");
		Assert.assertEquals(displayProgramMessagesText, terminalSession
				.getSnapshot().toString());
	}

}
