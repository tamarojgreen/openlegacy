package org.openlegacy.designtime.terminal.analyzer.support;

import freemarker.template.TemplateException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsLoader;
import org.openlegacy.designtime.terminal.generators.ScreenEntityJavaGenerator;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = loadAndAssertDefinitions();

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

	private Map<String, ScreenEntityDefinition> loadAndAssertDefinitions() {
		snapshotsSorter.setMatchingPercent(99);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(getClass().getResource("mock").getFile());
		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = snapshotsAnalyzer.analyzeSnapshots(snapshots);
		Assert.assertEquals(3, screenEntitiesDefinitions.size());
		return screenEntitiesDefinitions;
	}

	@Test
	public void testBasicTable() {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = loadAndAssertDefinitions();

		ScreenEntityDefinition tableScreen = screenEntitiesDefinitions.get("TableScreen");
		Assert.assertNotNull(tableScreen);
		Map<String, TableDefinition> tablesDefinitions = tableScreen.getTableDefinitions();
		Assert.assertEquals(1, tablesDefinitions.size());
		TableDefinition table1 = tablesDefinitions.get("TableScreenRow");

		Assert.assertEquals(5, table1.getStartRow());
		Assert.assertEquals(7, table1.getEndRow());

		ColumnDefinition columnSelction = table1.getColumnDefinition("action");
		Assert.assertNotNull(columnSelction);
		Assert.assertEquals(4, columnSelction.getStartColumn());
		Assert.assertEquals(5, columnSelction.getEndColumn());
		Assert.assertTrue(columnSelction.isEditable());
		Assert.assertEquals("Action", columnSelction.getDisplayName());

		ColumnDefinition columnA = table1.getColumnDefinition("columnA");
		Assert.assertNotNull(columnA);
		Assert.assertEquals(11, columnA.getStartColumn());
		Assert.assertEquals(19, columnA.getEndColumn());
		Assert.assertEquals("Column A", columnA.getDisplayName());
		Assert.assertEquals("Cell 1A", columnA.getSampleValue());

		ColumnDefinition columnB = table1.getColumnDefinition("columnB");
		Assert.assertNotNull(columnB);
		Assert.assertEquals(21, columnB.getStartColumn());
		Assert.assertEquals(29, columnB.getEndColumn());
		Assert.assertEquals("Column B", columnB.getDisplayName());
		Assert.assertEquals("Cell 1B", columnB.getSampleValue());

		ColumnDefinition columnC = table1.getColumnDefinition("column4");
		Assert.assertNotNull(columnC);
		Assert.assertEquals(31, columnC.getStartColumn());
		Assert.assertEquals(39, columnC.getEndColumn());
		Assert.assertEquals("Column4", columnC.getDisplayName());
		Assert.assertEquals("Cell 1C", columnC.getSampleValue());
	}

	@Test
	public void testGenerate() throws TemplateException, IOException {

		Map<String, ScreenEntityDefinition> screenEntitiesDefinitions = loadAndAssertDefinitions();

		assertScreenContent(screenEntitiesDefinitions.get("Screen1"));
		assertScreenContent(screenEntitiesDefinitions.get("Screen2"));
		assertScreenContent(screenEntitiesDefinitions.get("TableScreen"));
	}

	private static void assertScreenContent(ScreenEntityDefinition screen) throws TemplateException, IOException {
		((ScreenEntityDesigntimeDefinition)screen).setPackageName("com.test");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ScreenEntityJavaGenerator().generate(screen, baos);
		System.out.println(new String(baos.toByteArray()));
	}
}
