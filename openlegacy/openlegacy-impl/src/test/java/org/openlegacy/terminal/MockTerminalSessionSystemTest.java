package org.openlegacy.terminal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration("/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockTerminalSessionSystemTest extends AbstractAS400TerminalSessionSystemTest {

	@Test
	public void testMockSystem() throws IOException {
		testAS400InventorySystem();
	}

}
