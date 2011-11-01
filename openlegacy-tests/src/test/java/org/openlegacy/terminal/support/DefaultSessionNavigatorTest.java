package org.openlegacy.terminal.support;

import com.someorg.examples.screens.ItemsList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.login.LoginModule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultSessionNavigatorTest extends AbstractTest {

	@Test
	public void testNavigation() {
		terminalSession.getModule(LoginModule.class).login("user", "pwd");

		Assert.assertTrue(terminalSession.isConnected());

		ItemsList itemsList = terminalSession.getEntity(ItemsList.class);
		Assert.assertNotNull(itemsList);
	}
}
