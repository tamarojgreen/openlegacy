package org.openlegacy.providers.tn5250j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.AbstractAS400TerminalSessionSystemTest;
import org.openlegacy.terminal.TerminalSession;
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

	@Test
	public void testTn5250jSystem() throws Exception {
		testAS400InventorySystem();
	}

	@Test
	public void testOnline() throws InterruptedException, IOException {
		terminalConnectionFactory.getProperties().put("SESSION_HOST", "as400.openlegacy.org");
		TerminalSession terminalSession = newTerminalSession();
		logger.debug("Screen recieved:");
		logger.debug(terminalSession.getSnapshot().toString());
		logger.debug("Sequence=" + terminalSession.getSequence());

		// wait 3 seconds
		Thread.sleep(3000);

		logger.debug(terminalSession.getSnapshot().toString());
		logger.debug("Sequence=" + terminalSession.getSequence());

		// verify we got the full screen after the 2 seconds wait
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("/online.expected"));

		AssertUtils.assertContent(terminalSession.getSnapshot().toString().getBytes(), expectedBytes);
		terminalSession.disconnect();
	}
}
