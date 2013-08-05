package org.openlegacy.terminal.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.support.mock.ListNumEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration("ListNumTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ListNumTest extends AbstractTest {

	@Test
	public void testListField() {

		TerminalSession terminalSession = newTerminalSession();

		List<Integer> expectedResult = new ArrayList<Integer>();
		expectedResult.add(1);
		expectedResult.add(2);
		expectedResult.add(123);

		ListNumEntity listNumScreen = terminalSession.getEntity(ListNumEntity.class);

		Assert.assertEquals(expectedResult, listNumScreen.getIntList());

	}

}
