package org.openlegacy.terminal.modules.trail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.modules.trail.TrailModule;
import org.openlegacy.modules.trail.TrailWriter;
import org.openlegacy.terminal.actions.SendKeyActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class XmlTrailWriterTest extends AbstractTest {

	@Autowired
	private TrailWriter trailWriter;

	@Test
	public void testTrail() {
		terminalSession.doAction(SendKeyActions.ENTER, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trailWriter.write(terminalSession.getModule(TrailModule.class).getSessionTrail(), baos);
		System.out.println(new String(baos.toByteArray()));
	}
}
