package org.openlegacy.terminal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MockTerminalConnectionTest extends AbstractTest {

	@Inject
	TerminalSession terminalSession;

	@Test
	public void testMockConnection() throws IOException {

		terminalSession.doAction(TerminalActions.ENTER());
	}

}
