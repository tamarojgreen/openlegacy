package org.openlegacy.designtime.generators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.analyzer.SnapshotsLoader;
import org.openlegacy.designtime.terminal.analyzer.support.DefaultTerminalSnapshotsOrganizer;
import org.openlegacy.terminal.TerminalSnapshot;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

@ContextConfiguration(locations = "/openlegacy-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsSorterTest {

	@Inject
	private DefaultTerminalSnapshotsOrganizer snapshotsSorter;

	@Inject
	private SnapshotsLoader<TerminalSnapshot> snapshotsLoader;

	@Before
	public void setup() {
		snapshotsSorter.clear();
	}

	@Test
	public void testGroupMatch() throws FileNotFoundException, JAXBException {
		String mockPath = getClass().getResource("mock").getFile();
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(mockPath);

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
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(mockPath);

		snapshotsSorter.setMatchingPercent(93);

		snapshotsSorter.add(snapshots);

		Assert.assertEquals(11, snapshotsSorter.getGroups().size());
		Iterator<Set<TerminalSnapshot>> iterator = snapshotsSorter.getGroups().iterator();
		// Inventory management
		Assert.assertEquals(3, iterator.next().size());
		// item details 1
		Assert.assertEquals(2, iterator.next().size());
		// item details 1
		Assert.assertEquals(2, iterator.next().size());
		// items list
		Assert.assertEquals(3, iterator.next().size());
		// main menu
		Assert.assertEquals(2, iterator.next().size());
		// sign on
		Assert.assertEquals(2, iterator.next().size());
		// warehouse type list
		Assert.assertEquals(1, iterator.next().size());

	}
}
