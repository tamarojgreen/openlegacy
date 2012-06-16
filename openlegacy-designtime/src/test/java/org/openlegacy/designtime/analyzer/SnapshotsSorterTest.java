package org.openlegacy.designtime.analyzer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.FieldFormatter;
import org.openlegacy.designtime.terminal.analyzer.support.DefaultTerminalSnapshotsOrganizer;
import org.openlegacy.designtime.terminal.analyzer.support.MostPopulatedSnapshotPickerStrategy;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshotsLoader;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SnapshotsSorterTest {

	@Inject
	private DefaultTerminalSnapshotsOrganizer snapshotsOrganizer;

	@Inject
	private TerminalSnapshotsLoader snapshotsLoader;

	@Inject
	private FieldFormatter fieldFormatter;

	@Test
	public void testSortByContent() {
		snapshotsOrganizer.clear();
		String mockPath = getClass().getResource("mock").getFile();
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(mockPath);

		snapshotsOrganizer.add(snapshots);

		Collection<Set<TerminalSnapshot>> groups = snapshotsOrganizer.getGroups();
		Assert.assertEquals(1, groups.size());
		Collection<TerminalSnapshot> representingSnapshots = snapshotsOrganizer.getGroupsRepresenters(new MostPopulatedSnapshotPickerStrategy(
				fieldFormatter));
		Assert.assertEquals(1, representingSnapshots.size());

		findMatch(representingSnapshots.iterator().next(), "AABBCC");
	}

	private static void findMatch(TerminalSnapshot terminalSnapshot, String string) {
		terminalSnapshot.getField(SimpleTerminalPosition.newInstance(1, 13)).getValue().equals(string);

	}
}
