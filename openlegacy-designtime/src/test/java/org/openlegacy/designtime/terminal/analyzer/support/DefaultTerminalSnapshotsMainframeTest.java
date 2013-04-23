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

import junit.framework.Assert;

@ContextConfiguration("DefaultTerminalSnapshotsMainframeTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsMainframeTest extends AbstractAnalyzerTest {

	@Test
	public void testMainframeAppGenerate() throws TemplateException, IOException {
		snapshotsOrganizer.setMatchingPercent(95);
		Map<String, ScreenEntityDefinition> definitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"mainframe.trail"));
		Collection<ScreenEntityDefinition> values = definitions.values();
		Assert.assertEquals(2, values.size());
		assertScreenContent(definitions.get("DallasTexas"), "mainframe/DallasTexas.java.expected");
		assertScreenContent(definitions.get("SystemLogon"), "mainframe/SystemLogon.java.expected");

	}

}
