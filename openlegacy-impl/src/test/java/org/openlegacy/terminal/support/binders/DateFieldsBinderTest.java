package org.openlegacy.terminal.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.Screen1;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DateFieldsBinderTest extends AbstractTest {

	@Test
	public void testFillDateField() {
		TerminalSession terminalSession = newTerminalSession();

		Screen1 screen1 = terminalSession.getEntity(Screen1.class);
		Calendar calendar = new GregorianCalendar(11, 10 - 1, 9); // 9/Oct/11
		Assert.assertEquals(calendar.getTime(), screen1.getDateField());

	}

	@Test
	public void testSendDateField() {
		TerminalSession terminalSession = newTerminalSession();

		Screen1 screen1 = terminalSession.getEntity(Screen1.class);
		Calendar calendar = new GregorianCalendar(11, 8 - 1, 9); // 9/Aug/11
		screen1.setDateField(calendar.getTime());

		try {
			terminalSession.doAction(TerminalActions.ENTER(), screen1);
		} catch (SessionEndedException e) {
			// OK
		}

	}
}
