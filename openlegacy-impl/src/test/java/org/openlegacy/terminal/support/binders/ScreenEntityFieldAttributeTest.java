package org.openlegacy.terminal.support.binders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.support.mock.ScreenDynamicFieldAttributeEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@ContextConfiguration("FieldAttributeType-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityFieldAttributeTest extends AbstractTest {

	@Test
	public void testEditableAttribute() {
		TerminalSession terminalSession = newTerminalSession();

		ScreenDynamicFieldAttributeEntity isEditableField = terminalSession.getEntity(ScreenDynamicFieldAttributeEntity.class);
		Boolean result = isEditableField.getInEditModeTrue();
		Assert.assertTrue(result);
		Boolean result2 = isEditableField.getInEditModeFalse();
		Assert.assertFalse(result2);
		Color filedColor = isEditableField.getRedField();
		Assert.assertTrue(filedColor == Color.RED);

	}

}
