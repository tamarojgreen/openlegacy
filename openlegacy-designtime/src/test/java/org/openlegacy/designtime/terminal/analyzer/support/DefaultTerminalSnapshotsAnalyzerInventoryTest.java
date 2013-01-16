package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsAnalyzerInventoryTest extends AbstractAnalyzerTest {

	@Test
	public void testInventoryAppGenerate() throws TemplateException, IOException {
		snapshotsOrganizer.setMatchingPercent(95);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(
				getClass().getResource("/apps/inventory/screens").getFile(), "MainMenu.xml");
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		assertScreenContent(screenEntitiesDefinitions.get("DemoEnvironment"), "inventory/DemoEnvironment.java.expected");

	}

}
