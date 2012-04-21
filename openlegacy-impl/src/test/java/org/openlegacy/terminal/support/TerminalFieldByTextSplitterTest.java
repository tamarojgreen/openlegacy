package org.openlegacy.terminal.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldsSplitter;
import org.openlegacy.terminal.TerminalSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TerminalFieldByTextSplitterTest extends AbstractTest {

	@Inject
	private TerminalFieldsSplitter terminalFieldsSplitter;

	@Test
	public void testFieldsSplit() {
		TerminalSession terminalSession = newTerminalSession();
		List<TerminalField> fields = terminalFieldsSplitter.splitFields(terminalSession.getSnapshot());
		Assert.assertEquals(3, fields.size());
		Assert.assertEquals("part 1  ", fields.get(0).getValue());
		Assert.assertEquals(10, fields.get(0).getPosition().getColumn());
		Assert.assertEquals(17, fields.get(0).getEndPosition().getColumn());

		Assert.assertEquals("part 2   ", fields.get(1).getValue());
		Assert.assertEquals(18, fields.get(1).getPosition().getColumn());
		Assert.assertEquals(26, fields.get(1).getEndPosition().getColumn());

		Assert.assertEquals("part 3", fields.get(2).getValue());
		Assert.assertEquals(27, fields.get(2).getPosition().getColumn());
		Assert.assertEquals(32, fields.get(2).getEndPosition().getColumn());
	}
}
