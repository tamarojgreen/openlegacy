package org.openlegacy.applinx;

import apps.inventory.screens.MainMenu;
import apps.inventory.screens.SignOn;

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ApxScreensRecognizerTest extends AbstractTest {

	@Autowired
	private ApxServerLoader apxServerLoader;

	@Test
	public void testScreenRecognizer() throws IOException, GXDesignTimeException {

		ApxTestUtil.createScreen(apxServerLoader, "SignOn", "Sign On");
		ApxTestUtil.createScreen(apxServerLoader, "MainMenu", "ApplinX Demo Environment");

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		Assert.assertNotNull(signOn);

		terminalSession.doAction(SendKeyActions.ENTER, null);
		MainMenu mainMenu = terminalSession.getEntity(MainMenu.class);
		Assert.assertNotNull(mainMenu);
	}

}
