package org.openlegacy.providers.applinx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.AbstractAS400TerminalSessionSystemTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ApxTerminalSessionSystemTest extends AbstractAS400TerminalSessionSystemTest {

	@Inject
	private ApxServerLoader apxServerLoader;

	@Test
	public void testApxSystem() throws Exception {
		ApxUtils.importRepository(apxServerLoader, getClass().getResource("/inventory.gxz"));
		testAS400InventorySystem();
	}

}
