package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Test;
import org.junit.runner.RunWith;
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

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AS400MenusTest extends AbstractAnalyzerTest {

	@Test
	public void testAS400Menus() throws TemplateException, IOException, JAXBException {
		snapshotsOrganizer.setMatchingPercent(95);
		TerminalSessionTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class,
				getClass().getResourceAsStream("AS400_menus.trail"));

		List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();

		// snapshot 5 - messages
		snapshots.add(trail.getSnapshots().get(0));

		// snapshot 5 - messages
		snapshots.add(trail.getSnapshots().get(4));

		// snapshot 9 - AS/400 main menu (+outgoing screen)
		snapshots.add(trail.getSnapshots().get(8));
		snapshots.add(trail.getSnapshots().get(9));

		// snapshot 11 - AS/400 sub menu
		snapshots.add(trail.getSnapshots().get(10));

		// snapshot 15 - form screen
		snapshots.add(trail.getSnapshots().get(14));

		Map<String, ScreenEntityDefinition> entityDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);

		// assertScreenContent(entityDefinitions.get("SignOn"), null);

		assertScreenContent(entityDefinitions.get("DisplayMessages"), "as400menus/DisplayMessages.java.expected");

		assertScreenContent(entityDefinitions.get("IbmIMainMenu"), "as400menus/IbmIMainMenu.java.expected");

		assertScreenContent(entityDefinitions.get("UserTasks"), "as400menus/UserTasks.java.expected");

		assertScreenContent(entityDefinitions.get("DisplayJobStatusAttributes"),
				"as400menus/DisplayJobStatusAttributes.java.expected");
	}

}
