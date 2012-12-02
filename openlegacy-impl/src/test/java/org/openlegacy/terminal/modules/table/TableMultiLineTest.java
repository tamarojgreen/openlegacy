package org.openlegacy.terminal.modules.table;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.table.mockup.MultiLineTableEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("TableMultiLineTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TableMultiLineTest extends AbstractTest {

	@Test
	public void testMultiLine() {

		TerminalSession terminalSession = newTerminalSession();

		MultiLineTableEntity multiLineScreen = terminalSession.getEntity(MultiLineTableEntity.class);
		
		multiLineScreen.getItemListRows().get(0).getItemDescription();
		String itemDescription = multiLineScreen.getItemListRows().get(0).getItemDescription();
		System.out.println(itemDescription);
		Assert.assertEquals("Domino Cubes - Board Games",itemDescription);
		itemDescription = multiLineScreen.getItemListRows().get(1).getItemDescription();
		System.out.println(itemDescription);
		Assert.assertEquals("Baby Bowling Game - Baby Toys",itemDescription);
		
	}

}
