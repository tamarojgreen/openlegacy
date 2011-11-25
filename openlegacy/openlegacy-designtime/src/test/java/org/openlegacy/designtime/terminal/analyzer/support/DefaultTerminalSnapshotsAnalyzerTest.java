package org.openlegacy.designtime.terminal.analyzer.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsLoader;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("/openlegacy-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsAnalyzerTest {

	@Inject
	private TerminalSnapshotsLoader snapshotsLoader;

	@Inject
	private DefaultTerminalSnapshotsAnalyzer snapshotsAnalyzer;

	@Inject
	private DefaultTerminalSnapshotsSorter snapshotsSorter;

	@Test
	public void testBasicAnalisys() {

		snapshotsSorter.setMatchingPercent(99);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(getClass().getResource("mock").getFile(), "Screen1.xml",
				"Screen2.xml");
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		Assert.assertEquals(2, screenEntitiesDefinitions.size());

		ScreenEntityDefinition screen1 = screenEntitiesDefinitions.get("Screen1");
		Assert.assertNotNull(screen1);
		Map<String, FieldMappingDefinition> fieldsDefinitions = screen1.getFieldsDefinitions();
		Assert.assertEquals(1, fieldsDefinitions.size());
		FieldMappingDefinition fieldA = fieldsDefinitions.get("fieldA");
		Assert.assertNotNull(fieldA);
		Assert.assertTrue(fieldA.isEditable());
		Assert.assertEquals("Field A", fieldA.getDisplayName());
		Assert.assertEquals(SimpleScreenPosition.newInstance(4, 13), fieldA.getScreenPosition());

	}

	@Test
	public void testBasicTable() {

		snapshotsSorter.setMatchingPercent(99);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(getClass().getResource("mock").getFile(),
				"TableScreen.xml", "Screen1.xml"); // TODO currently identification works when having more then 1 screen
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		Assert.assertEquals(2, screenEntitiesDefinitions.size());

		ScreenEntityDefinition tableScreen = screenEntitiesDefinitions.get("TableScreen");
		Assert.assertNotNull(tableScreen);
		Map<String, TableDefinition> tablesDefinitions = tableScreen.getTableDefinitions();
		Assert.assertEquals(1, tablesDefinitions.size());
		TableDefinition table1 = tablesDefinitions.get("table1");

		Assert.assertEquals(5, table1.getStartRow());
		Assert.assertEquals(7, table1.getEndRow());

		ColumnDefinition columnSelction = table1.getColumnDefinition("selection");
		Assert.assertNotNull(columnSelction);
		Assert.assertEquals(2, columnSelction.getStartColumn());
		Assert.assertEquals(3, columnSelction.getEndColumn());
		Assert.assertTrue(columnSelction.isEditable());
		Assert.assertEquals("Selection", columnSelction.getDisplayName());

		ColumnDefinition columnA = table1.getColumnDefinition("columnA");
		Assert.assertNotNull(columnA);
		Assert.assertEquals(5, columnA.getStartColumn());
		Assert.assertEquals(13, columnA.getEndColumn());
		Assert.assertEquals("Column A", columnA.getDisplayName());
		Assert.assertEquals("Cell 1A", columnA.getSampleValue());

		ColumnDefinition columnB = table1.getColumnDefinition("columnB");
		Assert.assertNotNull(columnB);
		Assert.assertEquals(15, columnB.getStartColumn());
		Assert.assertEquals(23, columnB.getEndColumn());
		Assert.assertEquals("Column B", columnB.getDisplayName());
		Assert.assertEquals("Cell 1B", columnB.getSampleValue());

	}
}
