package org.openlegacy.providers.h3270;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("H3270TerminalSessionRtlTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class H3270TerminalSessionRtlTest extends AbstractTest {

	@Test
	public void testH3270System() throws Exception {
		TerminalSession terminalSession = newTerminalSession();
		TerminalSnapshot snapshot = terminalSession.getSnapshot();
		System.out.println(snapshot);
		terminalSession.flip();
		snapshot = terminalSession.getSnapshot();
		System.out.println(snapshot);
	}

}
