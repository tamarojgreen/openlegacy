package org.openlegacy.terminal.modules.trail;

import apps.inventory.screens.SignOn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.actions.TerminalActions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class XmlTrailWriterTest extends AbstractTest {

	@Inject
	private TrailWriter trailWriter;

	@Test
	public void testTrail() {
		SignOn signOn = terminalSession.getEntity(SignOn.class);
		signOn.setUser("user");
		terminalSession.doAction(TerminalActions.ENTER(), signOn);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(terminalSession.getModule(Trail.class).getSessionTrail(), baos);
		String result = new String(baos.toByteArray());

		String userSent = "<field value=\"user\" length=\"10\" modified=\"true\" editable=\"true\">";
		Assert.assertTrue(result.contains(userSent));

		System.out.println(result);

	}
}
