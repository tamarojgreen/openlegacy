package org.openlegacy.designtime.generators;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.analyzer.SnapshotsLoader;
import org.openlegacy.designtime.terminal.analyzer.support.DefaultTerminalSnapshotsSorter;
import org.openlegacy.terminal.TerminalSnapshot;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsSorterTest {

	@Inject
	private DefaultTerminalSnapshotsSorter snapshotsSorter;

	@Inject
	private SnapshotsLoader<TerminalSnapshot> snapshotsLoader;

	@Test
	public void testGroupMatch() throws FileNotFoundException, JAXBException {
		String mockPath = getClass().getResource("mock").getFile();
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadAll(mockPath);

		snapshotsSorter.setMatchingPercent(99);

		snapshotsSorter.add(snapshots);

		Assert.assertEquals(2, snapshotsSorter.getGroups().size());
		Iterator<Set<TerminalSnapshot>> iterator = snapshotsSorter.getGroups().iterator();
		Assert.assertEquals(2, iterator.next().size());
		Assert.assertEquals(2, iterator.next().size());
	}

	@Test
	public void testComplexMatch() throws FileNotFoundException, JAXBException {
		String mockPath = getClass().getResource("/apps/inventory/screens_xml").getFile();
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadAll(mockPath);

		snapshotsSorter.setMatchingPercent(93);

		snapshotsSorter.add(snapshots);

		Assert.assertEquals(7, snapshotsSorter.getGroups().size());
		Iterator<Set<TerminalSnapshot>> iterator = snapshotsSorter.getGroups().iterator();
		Assert.assertEquals(1, iterator.next().size());
		Assert.assertEquals(2, iterator.next().size());
		Assert.assertEquals(2, iterator.next().size());
		Assert.assertEquals(4, iterator.next().size());
		Assert.assertEquals(1, iterator.next().size());
		Assert.assertEquals(2, iterator.next().size());
		Assert.assertEquals(4, iterator.next().size());

	}
}
