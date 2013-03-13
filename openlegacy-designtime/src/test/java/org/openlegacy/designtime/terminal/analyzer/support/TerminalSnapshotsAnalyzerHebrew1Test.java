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

@ContextConfiguration("TerminalSnapshotsAnalyzerHebrew1Test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TerminalSnapshotsAnalyzerHebrew1Test extends AbstractAnalyzerTest {

	@Test
	public void testHebew1AppGenerate() throws TemplateException, IOException, JAXBException {
		snapshotsOrganizer.setMatchingPercent(95);

		TerminalSessionTrail trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class,
				getClass().getResourceAsStream("bidi/hebrew1.trail"));

		List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();

		for (int i = 0; i < 29; i++) {
			snapshots.add(trail.getSnapshots().get(i));
		}

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		assertScreenContent(screenEntitiesDefinitions.get("CniseLmarctEzmnotLkohRstotSiook"),
				"hebrew1/CniseLmarctEzmnotLkohRstotSiook.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("EkmeThzokEzmnotLkoh"), "hebrew1/EkmeThzokEzmnotLkoh.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("CniseLmarctEzmnotLkohRstotSiook"),
				"hebrew1/CniseLmarctEzmnotLkohRstotSiook.java.expected");

	}
}
