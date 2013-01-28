package org.openlegacy.terminal.support.binders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.support.mock.ConditinalFieldEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("conditionalFieldsBinderTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ConditionalFieldTest extends AbstractTest {

	@Test
	public void testConditionalFields() {
		TerminalSession terminalSession = newTerminalSession();

		ConditinalFieldEntity conditinalFieldEntity = terminalSession.getEntity(ConditinalFieldEntity.class);
		Assert.assertEquals(conditinalFieldEntity.getWhenTrue(), "IF MATCHED");
		Assert.assertNull(conditinalFieldEntity.getWhenFalse());
		Assert.assertNull(conditinalFieldEntity.getUnlessTrue());
		Assert.assertEquals(conditinalFieldEntity.getUnlessFalse(), "UNLESSE NOT MATCH");
		Assert.assertNull(conditinalFieldEntity.getBothTrue());
		Assert.assertNull(conditinalFieldEntity.getBothFalse());
		Assert.assertEquals(conditinalFieldEntity.getNone(), "NO CONDITION");

	}

}
