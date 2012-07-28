package org.openlegacy.terminal.web;

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

@ContextConfiguration("classpath*:/test-web-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JsonSerializationUtilTest {

	@Inject
	private TerminalSnapshotsLoader snapshotsLoader;

	@Test
	public void testSnapshotJsonSerialization() throws IOException {

		List<TerminalSnapshot> snapshots = snapshotsLoader.loadSnapshots(getClass().getResource("/inventory").getFile(),
				"SignOn.xml");

		String result = JsonSerializationUtil.toJson(snapshots.get(0));

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("json.expected"));
		AssertUtils.assertContent(expectedBytes, result.getBytes());
	}
}
