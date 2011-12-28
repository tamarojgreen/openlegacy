package org.openlegacy.terminal.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath*:/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JsonSerializationUtilTest extends AbstractTest {

	@Test
	public void testSnapshotJsonSerialization() {
		TerminalSession terminalSession = newTerminalSession();
		String result = JsonSerializationUtil.toJson(terminalSession.getSnapshot());
		System.out.println(result);
	}
}
