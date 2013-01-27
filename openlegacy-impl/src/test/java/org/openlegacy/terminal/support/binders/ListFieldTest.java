package org.openlegacy.terminal.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.support.mock.ListFieldEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration("ListFieldTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ListFieldTest extends AbstractTest {

	@Test
	public void testListField() {

		TerminalSession terminalSession = newTerminalSession();

		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add("Domino");
		expectedResult.add("Cube");
		expectedResult.add("Sevivon");

		ListFieldEntity listLineScreen = terminalSession.getEntity(ListFieldEntity.class);

		Assert.assertEquals(expectedResult, listLineScreen.getToysList());

	}

	@Test
	public void testSendListAndArrayField() {
		List<String> listNewValues = new ArrayList<String>();
		listNewValues.add("Domino1");
		listNewValues.add("Cube2");
		listNewValues.add("Sevivon3");

		String[] arrayNewValues = new String[3];
		arrayNewValues[0] = "Barbi1";
		arrayNewValues[1] = "Winx2";
		arrayNewValues[2] = "Remi3";

		TerminalSession terminalSession = newTerminalSession();

		ListFieldEntity listScreenEntity = terminalSession.getEntity(ListFieldEntity.class);
		listScreenEntity.setToysList(listNewValues);
		listScreenEntity.setToysArray(arrayNewValues);

		try {
			terminalSession.doAction(TerminalActions.ENTER(), listScreenEntity);
		} catch (SessionEndedException e) {
			// OK
		}
	}

	@Test
	public void testArrayField() {

		TerminalSession terminalSession = newTerminalSession();

		String[] expectedResult = new String[3];
		expectedResult[0] = "Barbi";
		expectedResult[1] = "Winx";
		expectedResult[2] = "Remi";

		ListFieldEntity listLineScreen = terminalSession.getEntity(ListFieldEntity.class);

		Assert.assertArrayEquals(expectedResult, listLineScreen.getToysArray());

	}

}
