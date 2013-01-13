package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsAnalyzer;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Map;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoSessionAnalyzerTest extends AbstractAnalyzerTest {

	@Ignore("Fails mysteriously due to tests order. Works standalone")
	@Test
	public void testInventoryAppTrail() throws TemplateException, IOException {

		TerminalSnapshotsAnalyzer snapshotsAnalyzer = applicationContext.getBean(TerminalSnapshotsAnalyzer.class);
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream(
				"demo_session.trail"));

		// checks for navigation generation
		assertScreenContent(screenEntitiesDefinitions.get("ItemDetails"), "inventory/ItemDetails.java.expected");
		assertScreenContent(screenEntitiesDefinitions.get("Items"), "inventory/Items.java.expected");
	}

}
