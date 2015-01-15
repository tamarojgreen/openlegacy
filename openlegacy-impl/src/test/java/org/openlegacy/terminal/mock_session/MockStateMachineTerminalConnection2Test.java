package org.openlegacy.terminal.mock_session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.mock_session.mock2.FormMock;
import org.openlegacy.terminal.mock_session.mock2.TableMock;
import org.openlegacy.terminal.mock_session.mock2.WindowMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import junit.framework.Assert;

@ContextConfiguration("MockStateMachineTerminalConnection2Test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockStateMachineTerminalConnection2Test extends AbstractTest {

	@Test
	public void testMockConnection() throws IOException {

		TerminalSession terminalSession = newTerminalSession();
		TableMock tableMock = terminalSession.getEntity(TableMock.class);
		Assert.assertNotNull(tableMock);

		// should know to get to snapshot 7 based on field change
		FormMock formMock = terminalSession.getEntity(FormMock.class, "DataB");
		Assert.assertTrue(7 == terminalSession.getSnapshot().getSequence());

		// should know to get to snapshot 3 based on field change
		formMock = terminalSession.getEntity(FormMock.class, "DataA");
		Assert.assertEquals("DataA", formMock.getField1());
		Assert.assertTrue(3 == terminalSession.getSnapshot().getSequence());

		WindowMock windowMock = terminalSession.getEntity(WindowMock.class);
		Assert.assertNotNull(windowMock);

	}
}
