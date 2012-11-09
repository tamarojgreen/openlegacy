package org.openlegacy.providers.tn5250j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration("/test-tn5250j-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Tn5250jHebrewTest extends AbstractTest {

	@Test
	public void testHebrew() throws Exception {
		TerminalSession terminalSession = newTerminalSession();
		System.out.println(terminalSession.getSnapshot());

		while (true) {
			terminalSession.doAction(TerminalActions.ENTER());
			System.out.println(terminalSession.getSnapshot());
		}
	}

	@Test
	public void testRightToLeftField() throws Exception {
		TerminalSession terminalSession = newTerminalSession();

		Tn5250jTerminalEditableField field = (Tn5250jTerminalEditableField)terminalSession.getSnapshot().getField(
				SimpleTerminalPosition.newInstance(21, 54));
		Assert.assertTrue(field.isEditable());
		Assert.assertTrue(field.isRightToLeft());

	}

}
