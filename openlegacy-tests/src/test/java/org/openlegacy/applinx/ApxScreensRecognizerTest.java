package org.openlegacy.applinx;

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

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;
import com.someorg.examples.screens.MainMenu;
import com.someorg.examples.screens.SignOn;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ApxScreensRecognizerTest extends AbstractTest {

	@Autowired
	private TerminalSession terminalSession;

	@Autowired
	private ApxServerLoader apxServerLoader;

	@Test
	public void testScreenRecognizer() throws IOException,
			GXDesignTimeException { 

		ApxTestUtil.createScreen(apxServerLoader, "SignOn", "Sign On");
		ApxTestUtil.createScreen(apxServerLoader, "MainMenu",
				"ApplinX Demo Environment");

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);

		terminalSession.doAction(SendKeyActions.ENTER);
		MainMenu mainMenu = terminalSession.getEntity(MainMenu.class);
		Assert.assertNotNull(mainMenu);
	}

}
