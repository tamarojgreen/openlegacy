package org.openlegacy.terminal.render;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshotsLoader;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("TerminalSnapshotImageRendererTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TerminalSnapshotImageRendererTest {

	@Inject
	private TerminalSnapshotsLoader terminalSnapshotsLoader;

	@Inject
	private TerminalSnapshotImageRenderer imageRenderer;

	@Ignore
	@Test
	public void testInventoryImages() throws IOException {
		testInventoryImage("InventoryManagement.xml", "InventoryManagement.jpg");
		testInventoryImage("WarehouseTypes.xml", "WarehouseTypes.jpg");
	}

	@Ignore
	@Test
	public void testBidiImage() throws IOException {
		testImage("/apps/bidi/", "BidiScreen.xml", "BidiScreen.jpg");
	}

	public void testInventoryImage(String xmlSnapshot, String expectedImageFile) throws IOException {
		testImage("/apps/inventory/screens/", xmlSnapshot, expectedImageFile);
	}

	public void testImage(String prefix, String xmlSnapshot, String expectedImageFileName) throws IOException {
		String file = getClass().getResource(prefix + xmlSnapshot).getFile();
		TerminalSnapshot snapshot = terminalSnapshotsLoader.load(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		imageRenderer.render(snapshot, baos);

		byte[] expectedImage = IOUtils.toByteArray(getClass().getResourceAsStream(prefix + expectedImageFileName));
		AssertUtils.assertImageContent(expectedImageFileName, expectedImage, baos.toByteArray());
	}

}
