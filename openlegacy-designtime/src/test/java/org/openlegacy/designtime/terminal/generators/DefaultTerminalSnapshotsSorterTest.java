package org.openlegacy.designtime.terminal.generators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.SnapshotsLoader;
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

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotsSorterTest {

	@Inject
	private DefaultTerminalSnapshotsOrganizer snapshotsOrganizer;

	@Inject
	private SnapshotsLoader<TerminalSnapshot> snapshotsLoader;

	@Before
	public void setup() {
		snapshotsOrganizer.clear();
	}

	@Test
	public void testGroupMatch() throws FileNotFoundException, JAXBException {
		String mockPath = getClass().getResource("mock").getFile();
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(mockPath);

		snapshotsOrganizer.setMatchingPercent(99);

		snapshotsOrganizer.add(snapshots);

		Assert.assertEquals(2, snapshotsOrganizer.getGroups().size());
		Iterator<Set<TerminalSnapshot>> iterator = snapshotsOrganizer.getGroups().iterator();
		Assert.assertEquals(2, iterator.next().size());
		Assert.assertEquals(2, iterator.next().size());
	}

	@Ignore("This tests needs refactoring. problem is that snapshots sets are not ordered, and this test break on different JDK's. No trivial way to check snapshot groups by Map, as snapshots group dont have a name/id")
	@Test
	public void testComplexMatch() throws FileNotFoundException, JAXBException {
		String inventoryPath = getClass().getResource("/apps/inventory/screens").getFile();
		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(inventoryPath);

		snapshotsOrganizer.setMatchingPercent(93);

		snapshotsOrganizer.add(snapshots);

		Assert.assertEquals(10, snapshotsOrganizer.getGroups().size());
		Iterator<Set<TerminalSnapshot>> iterator = snapshotsOrganizer.getGroups().iterator();
		// Inventory management
		Assert.assertEquals(1, iterator.next().size());
		// item details 1
		Assert.assertEquals(2, iterator.next().size());
		// item details 1
		Assert.assertEquals(2, iterator.next().size());
		// items list
		Assert.assertEquals(3, iterator.next().size());
		// main menu
		Assert.assertEquals(1, iterator.next().size());
		// sign on
		Assert.assertEquals(1, iterator.next().size());
		// warehouses list
		Assert.assertEquals(2, iterator.next().size());

	}
}
