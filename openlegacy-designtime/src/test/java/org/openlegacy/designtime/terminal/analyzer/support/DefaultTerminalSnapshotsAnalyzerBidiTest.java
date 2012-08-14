package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ContextConfiguration("DefaultTerminalSnapshotsAnalyzerBidiTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsAnalyzerBidiTest extends AbstractAnalyzerTest {

	@Test
	public void testBidiScreens() throws TemplateException, IOException {
		//
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(
				getClass().getResource("/org/openlegacy/designtime/terminal/analyzer/support/bidi").getFile(),
				"BidiFormScreen.xml", "BidiMenu.xml");

		snapshotsOrganizer.setMatchingPercent(99);
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);

		Assert.assertEquals(2, definitions.size());
		// means - "Hebrew Screen" in Hebrew
		assertScreenContent(definitions.get("MscBabrit"), "bidi/BidiFormScreen.java.expected");
		assertScreenContent(definitions.get("MscTprit"), "bidi/BidiMenu.java.expected");

	}

	@Ignore
	@Test
	public void testHebrewAS400AppGenerate() throws TemplateException, IOException {
		snapshotsOrganizer.setMatchingPercent(95);
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"hebrew_as400.trail.xml"));

		Assert.assertEquals(2, definitions.size());
		assertScreenContent(definitions.get("Mdd"), "bidi/HebrewWindowTable.java.expected");

	}

}
