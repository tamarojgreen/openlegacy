package org.openlegacy.providers.tn5250j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.AbstractAS400TerminalSessionSystemTest;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.SimpleConnectionProperties;
import org.openlegacy.terminal.support.SimpleTerminalSendAction;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("/test-tn5250j-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Tn5250jTerminalSessionSystemTest extends AbstractAS400TerminalSessionSystemTest {

	private final static Log logger = LogFactory.getLog(Tn5250jTerminalSessionSystemTest.class);

	@Inject
	private Tn5250jTerminalConnectionFactory terminalConnectionFactory;

	@Ignore("not available")
	@Test
	public void testTn5250jSystem() throws Exception {
		testAS400InventorySystem();
	}

	@Test
	public void testOnline() throws InterruptedException, IOException {
		terminalConnectionFactory.getProperties().put("SESSION_HOST", "as400.openlegacy.org");
		TerminalSession terminalSession = newTerminalSession();

		assertSnapshot(terminalSession.getSnapshot(), "/online1.expected");

		terminalSession.doAction(TerminalActions.ENTER());

		assertSnapshot(terminalSession.getSnapshot(), "/online2.expected");

		terminalSession.disconnect();
	}

	private void assertSnapshot(TerminalSnapshot terminalSnapshot, String snapshotTextFile) throws IOException {
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(snapshotTextFile));
		AssertUtils.assertContent(expectedBytes, terminalSnapshot.toString().getBytes());
	}

	@Test
	public void testDisconnect() throws InterruptedException {
		TerminalConnection connection = terminalConnectionFactory.getConnection(new SimpleConnectionProperties());
		while (true) {
			TerminalSendAction action = new SimpleTerminalSendAction("[enter]");
			connection.doAction(action);
			System.out.println(connection.getSnapshot());
			Thread.sleep(5000);
		}
	}
}
