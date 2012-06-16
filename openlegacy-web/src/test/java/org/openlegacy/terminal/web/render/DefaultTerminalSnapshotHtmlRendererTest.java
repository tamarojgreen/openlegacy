package org.openlegacy.terminal.web.render;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshotsLoader;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("/test-web-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultTerminalSnapshotHtmlRendererTest {

	@Inject
	private TerminalSnapshotHtmlRenderer htmlRenderer;

	@Inject
	private TerminalSnapshotsLoader snapshotsLoader;

	@Test
	public void testHtmlRender() throws IOException {

		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(getClass().getResource("/").getFile(), "SignOn.xml");

		String result = htmlRenderer.render(snapshots.get(0));

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("html.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());

	}
}
