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
				getClass().getResource("/apps/inventory/screens").getFile(), "SignOn.xml", "MainMenu.xml", "ItemsList.xml",
				"ItemDetails1.xml", "ItemDetails2.xml", "WarehouseTypes.xml");
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		assertScreenContent(screenEntitiesDefinitions.get("SignOn"), "SignOn.java.expected");

		assertScreenContent(screenEntitiesDefinitions.get("DemoEnvironment"), "inventory/DemoEnvironment.java.expected");

		// table
		assertScreenContent(screenEntitiesDefinitions.get("WorkWithItemMaster"), "inventory/WorkWithItemMaster.java.expected");
		// form1
		assertScreenContent(screenEntitiesDefinitions.get("WorkWithItemMaster1"), "inventory/WorkWithItemMaster1.java.expected");
		// form2
		assertScreenContent(screenEntitiesDefinitions.get("WorkWithItemMaster2"), "inventory/WorkWithItemMaster2.java.expected");
		// reversed window
		assertScreenContent(screenEntitiesDefinitions.get("ListOfWarehouseTypes"), "inventory/ListOfWarehouseTypes.java.expected");

	}

	@Test
	public void testInventoryAppTrail() throws TemplateException, IOException {
		// TODO Take ~35 seconds - need to improve rules execution time
		snapshotsAnalyzer.analyzeTrail(getClass().getResourceAsStream("inventory_session.trail"));
	}

}
