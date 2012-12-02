package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.modules.messages.Messages.MessageField;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.module.TerminalSessionTrail;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AS400MenusTest extends AbstractAnalyzerTest {

	@Test
	public void testAS400Menus() throws TemplateException, IOException, JAXBException {
		snapshotsOrganizer.setMatchingPercent(95);
		TerminalSessionTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class,
				getClass().getResourceAsStream("AS400_menus.trail"));

		List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();
		// messages
		snapshots.add(trail.getSnapshots().get(4));

		Map<String, ScreenEntityDefinition> entityDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);

		ScreenEntityDefinition messagesScreen = entityDefinitions.get("DisplayMessages");

		Assert.assertNotNull(messagesScreen.getFirstFieldDefinition(MessageField.class));
		Assert.assertEquals(6, messagesScreen.getActions().size());

	}

}
