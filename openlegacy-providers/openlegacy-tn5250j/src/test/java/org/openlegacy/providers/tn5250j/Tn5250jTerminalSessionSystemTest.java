package org.openlegacy.providers.tn5250j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.AbstractTerminalSessionSystemTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/test-tn5250j-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Tn5250jTerminalSessionSystemTest extends AbstractTerminalSessionSystemTest {

	@Test
	public void testTn5250jSystem() throws Exception {
		testSystem();
	}

}
