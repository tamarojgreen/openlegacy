package org.openlegacy.terminal.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSessionTest extends AbstractTest {

	@Test
	public void testConnection() {
		Assert.assertFalse(terminalSession.isConnected());

		terminalSession.getSnapshot();

		Assert.assertTrue(terminalSession.isConnected());

		terminalSession.disconnect();

		Assert.assertFalse(terminalSession.isConnected());
	}
}
