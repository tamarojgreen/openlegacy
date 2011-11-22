package org.openlegacy.designtime.analyzer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.analyzer.TerminalSnapshotsLoader;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration(value = "/openlegacy-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SnapshotsSorterTest {

	@Inject
	private SnapshotsSorter<TerminalSnapshot> snapshotsSorter;

	@Inject
	private TerminalSnapshotsLoader snapshotsLoader;

	@Test
	public void testSortByContent() {
		String mockPath = getClass().getResource("mock").getFile();
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadAll(mockPath);

		snapshotsSorter.add(snapshots);

		Collection<Set<TerminalSnapshot>> groups = snapshotsSorter.getGroups();
		Assert.assertEquals(1, groups);
		Set<TerminalSnapshot> group = groups.iterator().next();
		TerminalSnapshot[] snapshotsArray = group.toArray(new TerminalSnapshot[group.size()]);
		findMatch(snapshotsArray[0], "AA");
		findMatch(snapshotsArray[1], "AABB");
		findMatch(snapshotsArray[2], "AABBCCC");
	}

	private static void findMatch(TerminalSnapshot terminalSnapshot, String string) {
		terminalSnapshot.getField(SimpleScreenPosition.newInstance(1, 13)).getValue().equals(string);

	}
}
