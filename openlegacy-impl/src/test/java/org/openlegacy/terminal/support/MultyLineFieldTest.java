package org.openlegacy.terminal.support;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.mock.MockTerminalConnectionFactory;
import org.openlegacy.terminal.support.mock.MultyLineFieldScreen;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("MultyLineFieldTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MultyLineFieldTest extends AbstractTest {

	@Inject
	private MockTerminalConnectionFactory terminalConnectionFactory;
	
	@Test
	public void testCustomBinder() {
		TerminalSession terminalSession = newTerminalSession();

		MultyLineFieldScreen multyLineFieldScreen = terminalSession.getEntity(MultyLineFieldScreen.class);
		Assert.assertEquals("AA BB\nCC DD", multyLineFieldScreen.getMultlyLineField());

		Assert.assertEquals("This field\ncontinue in next line", multyLineFieldScreen.getMultlyLineBreakingField());

		// validated during the send
		multyLineFieldScreen.setMultlyLineField("A$ B$C$ D$");
		multyLineFieldScreen.setMultlyLineBreakingField("This field was modified");

		try {
			terminalSession.doAction(TerminalActions.ENTER(), multyLineFieldScreen);
		} catch (SessionEndedException e) {
			// OK
		}

	}

	@Before
	public void before() {
		terminalConnectionFactory.setVerifySend(true);
	}
	
	@Ignore("TODO. related to #218")
	@Test
	public void testShortContentInRectangle() {
		
		terminalConnectionFactory.setVerifySend(false);
		TerminalSession terminalSession = newTerminalSession();

		MultyLineFieldScreen multyLineFieldScreen = terminalSession.getEntity(MultyLineFieldScreen.class);

		// validated during the send
		multyLineFieldScreen.setMultlyLineField("TODO");

		try {
			terminalSession.doAction(TerminalActions.ENTER(), multyLineFieldScreen);
		} catch (SessionEndedException e) {
			// OK
		}
		

	}
}
