package org.openlegacy.terminal.support.binders;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.AbstractTest;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.mock.ScreenDynamicFieldAttributeEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration("FieldAttributeType-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityDynamicFieldAttributeTest extends AbstractTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;
	
	@Test
	public void testEditableAttribute() {
		ScreenEntityDefinition screen = screenEntitiesRegistry.get(ScreenDynamicFieldAttributeEntity.class);
		ScreenFieldDefinition field = screen.getFieldsDefinitions().get("inEditModeDynamic");
		String text = field.getDynamicFieldDefinition().getText();
		Assert.assertEquals("Test Content", text);
	}

}
