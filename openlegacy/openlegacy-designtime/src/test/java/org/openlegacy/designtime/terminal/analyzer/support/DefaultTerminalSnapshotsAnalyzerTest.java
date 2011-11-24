package org.openlegacy.designtime.terminal.analyzer.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsLoader;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
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
	public void testAnalyzer() {

		snapshotsSorter.setMatchingPercent(99);
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadAll(getClass().getResource("mock").getFile());
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
}
