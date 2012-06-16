package org.openlegacy.terminal.support;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.Screen1;
import org.openlegacy.terminal.support.mock.WindowScreen1;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for 2 screens: 1 main screen with title, and 2nd with a window in it. Verifies @ScreenEntity(widnow=true) gets higher
 * priority during snapshot identification process
 * 
 */
@ContextConfiguration("WindowTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class WindowTest extends AbstractTest {

	@Test
	public void testWindowIdentifiedFirst() {
		TerminalSession terminalSession = newTerminalSession();
		Assert.assertTrue(ProxyUtil.isClassesMatch(Screen1.class, terminalSession.getEntity().getClass()));
		terminalSession.doAction(TerminalActions.ENTER());
		Assert.assertTrue(ProxyUtil.isClassesMatch(WindowScreen1.class, terminalSession.getEntity().getClass()));
	}

}
