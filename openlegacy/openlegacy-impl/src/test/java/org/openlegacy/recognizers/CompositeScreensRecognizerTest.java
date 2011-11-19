package org.openlegacy.recognizers;

import apps.inventory.screens.SignOn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tests.recognizers.pattern.MainMenu;

import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CompositeScreensRecognizerTest extends AbstractTest {

	@Test
	public void testComposite() throws IOException {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);
		terminalSession.doAction(SendKeyActions.ENTER);
		MainMenu mainMenu = terminalSession.getEntity(MainMenu.class);
		Assert.assertNotNull(mainMenu);
	}
}
