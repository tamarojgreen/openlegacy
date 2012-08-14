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

@ContextConfiguration("DefaultTerminalSnapshotsAnalyzerHebrewTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsAnalyzerHebrewTest extends AbstractAnalyzerTest {

	@Test
	public void testBidiScreens() throws TemplateException, IOException {
		//
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(
				getClass().getResource("/org/openlegacy/designtime/terminal/analyzer/support/hebrew").getFile(),
				"BidiFormScreen.xml");

		snapshotsOrganizer.setMatchingPercent(95);
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);

		Assert.assertEquals(1, definitions.size());
		assertScreenContent(definitions.get("MscBabrit"), "hebrew/HebrewScreen.java.expected");

	}

	@Ignore
	@Test
	public void testHebrewAS400AppGenerate() throws TemplateException, IOException {
		snapshotsOrganizer.setMatchingPercent(95);
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"hebrew_as400.trail.xml"));

		Assert.assertEquals(2, definitions.size());
		assertScreenContent(definitions.get("Mdd"), "hebrew/HebrewWindowTable.java.expected");

	}

}
