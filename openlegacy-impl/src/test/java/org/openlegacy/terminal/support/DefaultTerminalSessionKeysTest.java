package org.openlegacy.terminal.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.support.mock.ScreenWithKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration("DefaultTerminalSessionKeysTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSessionKeysTest extends AbstractTest {

	@Test
	public void testGetEntityWithKeys() {
		TerminalSession terminalSession = newTerminalSession();
		ScreenWithKey screenWithKey = terminalSession.getEntity(ScreenWithKey.class, 11);
		Assert.assertEquals(11, screenWithKey.getKeyField().intValue());

		screenWithKey = terminalSession.getEntity(ScreenWithKey.class, 22);
		Assert.assertEquals(22, screenWithKey.getKeyField().intValue());
	}

}
