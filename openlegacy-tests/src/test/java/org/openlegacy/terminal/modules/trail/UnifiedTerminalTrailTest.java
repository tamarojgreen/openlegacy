package org.openlegacy.terminal.modules.trail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.support.actions.SendKeyActions;
import org.openlegacy.trail.TrailWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UnifiedTerminalTrailTest extends AbstractTest {

	@Autowired
	private TerminalSession terminalSession;

	@Autowired
	private TrailWriter trailWriter;

	@Test
	public void testTrail() {
		terminalSession.doAction(SendKeyActions.ENTER, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(terminalSession.getModule(SessionTrailModule.class).getSessionTrail(), baos);
		System.out.println(new String(baos.toByteArray()));
	}
}
