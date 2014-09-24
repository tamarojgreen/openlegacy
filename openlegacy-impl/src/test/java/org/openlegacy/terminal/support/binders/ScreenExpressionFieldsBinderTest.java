package org.openlegacy.terminal.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.support.mock.ScreenExpressionScreen;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the expression attribute of the ScreenField annotation
 * 
 * @author ryan eberly
 */
@ContextConfiguration("ScreenExpressionFieldsBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenExpressionFieldsBinderTest extends AbstractTest {

	@Test
	public void testFillScreenExpressionField() {
		TerminalSession terminalSession = newTerminalSession();

		ScreenExpressionScreen screen = terminalSession.getEntity(ScreenExpressionScreen.class);
		System.out.println(screen);
		Assert.assertEquals(new Integer(10000), screen.getTotalA());
		Assert.assertEquals(new Integer(5555), screen.getTotalB());
		Assert.assertEquals(new Integer(15555), screen.getCombinedTotalAandB());
		Assert.assertEquals(Boolean.TRUE, screen.getErrorOccurred());
		Assert.assertEquals(Boolean.FALSE, screen.getErrorOccurred2());
		Assert.assertEquals("Some Error occurred!", screen.getMessage());

	}
}
