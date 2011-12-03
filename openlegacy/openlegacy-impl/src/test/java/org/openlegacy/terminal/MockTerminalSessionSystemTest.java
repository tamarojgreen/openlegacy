package org.openlegacy.terminal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@ContextConfiguration(locations = "/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockTerminalSessionSystemTest extends AbstractTerminalSessionSystemTest {

	@Test
	public void testMockSystem() throws IOException {
		testSystem();
	}

}
