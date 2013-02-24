package org.openlegacy.terminal.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.DateScreen;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@ContextConfiguration("DateFieldsBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DateFieldsBinderTest extends AbstractTest {

	@Test
	public void testFillDateField() {
		TerminalSession terminalSession = newTerminalSession();

		DateScreen screen1 = terminalSession.getEntity(DateScreen.class);
		Calendar calendar = new GregorianCalendar(2011, 10 - 1, 9); // 9/Oct/11
		Assert.assertEquals(calendar.getTime(), screen1.getDateField());
		calendar = new GregorianCalendar(2013, 2 - 1, 11); // 11/Feb/13
		Assert.assertEquals(calendar.getTime(), screen1.getDateField2());
		Assert.assertEquals(calendar.getTime(), screen1.getDateField3());
		Assert.assertEquals(calendar.getTime(), screen1.getDateField4());
	}

	@Test
	public void testSendDateField() {
		TerminalSession terminalSession = newTerminalSession();

		DateScreen screen1 = terminalSession.getEntity(DateScreen.class);
		Calendar calendar = new GregorianCalendar(11, 8 - 1, 9); // 9/Aug/11
		screen1.setDateField(calendar.getTime());
		calendar = new GregorianCalendar(2013, 7 - 1, 11); // 11/jul/13
		Date date = calendar.getTime();
		screen1.setDateField2(date);
		screen1.setDateField3(date);
		screen1.setDateField4(date);
		try {
			terminalSession.doAction(TerminalActions.ENTER(), screen1);
		} catch (SessionEndedException e) {
			// OK
		}

	}
}
