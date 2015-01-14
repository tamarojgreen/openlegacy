package org.openlegacy.terminal.mock_session;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.mock_session.mock2.FormMock;
import org.openlegacy.terminal.mock_session.mock2.TableMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("MockStateMachineTerminalConnection2Test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MockStateMachineTerminalConnection2Test extends AbstractTest {

	@Test
	public void testMockConnection() throws IOException {

		TerminalSession terminalSession = newTerminalSession();
		TableMock tableMock = terminalSession.getEntity(TableMock.class);
		Assert.assertNotNull(tableMock);

		// should know to get to snapshot 7 based on focus
		FormMock formMock = terminalSession.getEntity(FormMock.class,"DataB");
		System.out.println(terminalSession.getSnapshot().getSequence()); 
		Assert.assertEquals("DataB", formMock.getField1());

		// should know to get to snapshot 3 based on focus
		formMock = terminalSession.getEntity(FormMock.class,"DataA");
		System.out.println(terminalSession.getSnapshot().getSequence()); 
		Assert.assertEquals("DataA", formMock.getField1());
		
	}
}
