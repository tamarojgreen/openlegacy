package org.openlegacy.recognizers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tests.recognizers.pattern.MainMenu;
import tests.recognizers.pattern.SignOn;

import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class PatternBasedScreensRecognizerTest extends AbstractTest {

	@Test
	public void testPattern() throws IOException {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);
		terminalSession.doAction(SendKeyActions.ENTER, null);
		MainMenu mainMenu = terminalSession.getEntity(MainMenu.class);
		Assert.assertNotNull(mainMenu);
	}

}
