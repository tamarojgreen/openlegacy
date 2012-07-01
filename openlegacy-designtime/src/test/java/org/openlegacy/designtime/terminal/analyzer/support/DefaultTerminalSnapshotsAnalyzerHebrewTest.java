package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@ContextConfiguration("DefaultTerminalSnapshotsAnalyzerHebrewTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsAnalyzerHebrewTest extends AbstractAnalyzerTest {

	@Test
	public void testHebrewAS400AppGenerate() throws TemplateException, IOException {
		snapshotsOrganizer.setMatchingPercent(95);
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"hebrew_as400.trail.xml"));
		Collection<ScreenEntityDefinition> values = definitions.values();
		for (ScreenEntityDefinition screenEntityDefinition : values) {
			assertScreenContent(screenEntityDefinition, null);
		}
		// Assert.assertEquals(2, values.size());
		// assertScreenContent(definitions.get("DallasTexas"), "mainframe/DallasTexas.java.expected");
		// assertScreenContent(definitions.get("SystemLogon"), "mainframe/SystemLogon.java.expected");

	}

}
