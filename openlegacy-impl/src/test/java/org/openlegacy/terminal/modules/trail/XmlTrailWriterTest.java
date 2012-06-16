package org.openlegacy.terminal.modules.trail;

import apps.inventory.screens.SignOn;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.test.utils.AssertUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class XmlTrailWriterTest extends AbstractTest {

	@Inject
	private TrailWriter trailWriter;

	@Test
	public void testTrail() throws IOException {
		TerminalSession terminalSession = newTerminalSession();

		SignOn signOn = terminalSession.getEntity(SignOn.class);
		signOn.setUser("user");
		terminalSession.doAction(TerminalActions.ENTER(), signOn);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(terminalSession.getModule(Trail.class).getSessionTrail(), baos);
		String result = StringUtil.toString(baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("trail.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());

	}
}
