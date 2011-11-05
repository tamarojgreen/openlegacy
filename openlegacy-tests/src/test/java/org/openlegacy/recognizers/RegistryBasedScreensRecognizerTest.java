package org.openlegacy.recognizers;


import apps.inventory.screens.MainMenu;
import apps.inventory.screens.SignOn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistryBasedScreensRecognizerTest extends AbstractTest {

	@Autowired
	private TerminalSession terminalSession;

	@Test
	public void testScreenRecognizer() throws IOException {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);

		terminalSession.doAction(SendKeyActions.ENTER, null);
		MainMenu mainMenu = terminalSession.getEntity(MainMenu.class);
		Assert.assertNotNull(mainMenu);
	}
}
