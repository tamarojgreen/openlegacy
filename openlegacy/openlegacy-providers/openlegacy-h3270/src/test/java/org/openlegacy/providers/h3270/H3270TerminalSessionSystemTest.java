package org.openlegacy.providers.h3270;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

@ContextConfiguration("/test-h3270-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class H3270TerminalSessionSystemTest extends AbstractTest {

	@Inject
	private TrailWriter trailWriter;

	@Test
	public void testH3270System() throws Exception {
		// TODO improve the test with some entities in the registry based on M/F application
		TerminalSession terminalSession = newTerminalSession();
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
		terminalSession.doAction(TerminalActions.ENTER());
	}

	@Test
	public void testH3270Trail() throws Exception {
		TerminalSession terminalSession = newTerminalSession();
		terminalSession.doAction(TerminalActions.ENTER());
		SessionTrail<? extends Snapshot> trail = terminalSession.getModule(Trail.class).getSessionTrail();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		trailWriter.write(trail, baos);
		System.out.println(new String(baos.toByteArray()));
	}

}
