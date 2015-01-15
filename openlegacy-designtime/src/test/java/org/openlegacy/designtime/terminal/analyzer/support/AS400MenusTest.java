package org.openlegacy.designtime.terminal.analyzer.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.module.TerminalSessionTrail;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import freemarker.template.TemplateException;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AS400MenusTest extends AbstractAnalyzerTest {

	@Test
	public void testAS400Menus() throws TemplateException, IOException, JAXBException {
		snapshotsOrganizer.setMatchingPercent(95);
		TerminalSessionTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class,
				getClass().getResourceAsStream("AS400_menus.trail"));

		List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();

		for (int i = 0; i < trail.getSnapshots().size(); i++) {
			snapshots.add(trail.getSnapshots().get(i));
		}

		Map<String, ScreenEntityDefinition> entityDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);

		// assertScreenContent(entityDefinitions.get("SignOn"), null);

		assertScreenContent(entityDefinitions.get("DisplayMessages"), "as400menus/DisplayMessages.java.expected");

		assertScreenContent(entityDefinitions.get("IbmIMainMenu"), "as400menus/IbmIMainMenu.java.expected");

		assertScreenContent(entityDefinitions.get("UserTasks"), "as400menus/UserTasks.java.expected");

		assertScreenContent(entityDefinitions.get("DisplayJobStatusAttributes"),
				"as400menus/DisplayJobStatusAttributes.java.expected");

		assertScreenContent(entityDefinitions.get("CopyFromPcDocumentcpyfrmpcd"),
				"as400menus/CopyFromPcDocumentcpyfrmpcd.java.expected");

		assertScreenContent(entityDefinitions.get("SendMessagesndmsg"), "as400menus/SendMessagesndmsg.java.expected");
		assertScreenContent(entityDefinitions.get("EditLibraryList"), "as400menus/EditLibraryList.java.expected");

		assertScreenContent(entityDefinitions.get("ChangeProfilechgprf"), "as400menus/ChangeProfilechgprf.java.expected");
		assertScreenContent(entityDefinitions.get("SubmitJobsbmjob"), "as400menus/SubmitJobsbmjob.java.expected");
		// assertScreenContent(entityDefinitions.get("WorkWithSubmittedJobs"), null);
	}

}
