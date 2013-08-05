package org.openlegacy.providers.h3270;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.Snapshot;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalSendAction;
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
		TerminalSession terminalSession = newTerminalSession();
		TerminalSnapshot snapshot = terminalSession.getSnapshot();

		TerminalField desiredTarget = snapshot.getField(SimpleTerminalPosition.newInstance(21, 17));
		desiredTarget.setValue("a");
		SimpleTerminalSendAction sendAction = new SimpleTerminalSendAction("enter");
		sendAction.getFields().add(desiredTarget);
		terminalSession.doAction(sendAction);

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
