package org.openlegacy.providers.applinx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.AbstractTerminalSessionSystemTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ApxTerminalSessionSystemTest extends AbstractTerminalSessionSystemTest {

	@Inject
	private ApxServerLoader apxServerLoader;

	@Test
	public void testApxSystem() throws Exception {
		ApxUtils.importRepository(apxServerLoader, getClass().getResource("/inventory.gxz"));
		testSystem();
	}

}
