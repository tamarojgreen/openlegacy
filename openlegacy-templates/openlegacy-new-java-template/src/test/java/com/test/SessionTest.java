package com.test;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/META-INF/spring/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SessionTest {

	@Inject
	private TerminalSession terminalSession;
	
	@Test
	public void testSession(){
		
		// Get the 1st screen in the session
		//SignOn signOn = terminalSession.getEntity(SignOn.class);
		
		// Verify screen received
		//Assert.assertNotNull(signOn);
		
		// set user & password
		//signOn.setUser("someuser");
		//signOn.setPassword("somepwd");
		
		// Send ENTER action on screen SignOn, and expected ManiMenu screen 
		//MainMenu mainMenu = terminalSession.doAction(TerminalActions.ENTER(),signOn,MainMenu.class);
		
		// Verify screen received
		//Assert.assertNotNull(mainMenu);
	}
}
