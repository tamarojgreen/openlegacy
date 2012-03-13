package org.openlegacy.terminal;

import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemDetails2;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.SignOn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.definitions.BooleanFieldTypeDefinition;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("/test-mock-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistryTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Test
	public void testIdentifiers() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(SignOn.class);
		ScreenIdentification screenIdentification = screenDefintion.getScreenIdentification();
		Assert.assertNotNull(screenIdentification);
		// 1 from class, 1 from super class
		Assert.assertEquals(2, screenIdentification.getScreenIdentifiers().size());
	}

	@Test
	public void testFields() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(SignOn.class);
		// 4 from class, 1 from super class
		Assert.assertEquals(6, screenDefintion.getFieldsDefinitions().size());
	}

	@Test
	public void testScreenPart() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(ItemDetails2.class);
		Assert.assertEquals(2, screenDefintion.getPartsDefinitions().size());
	}

	@Test
	public void testScreenTable() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(ItemsList.class);
		Assert.assertEquals(1, screenDefintion.getTableDefinitions().size());
	}

	@Test
	public void testActions() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(SignOn.class);
		// 1 from class, 1 from super class
		Assert.assertEquals(2, screenDefintion.getActions().size());
	}

	@Test
	public void testFieldTypes() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(ItemDetails1.class);
		ScreenFieldDefinition screenFieldDefinition = screenDefintion.getFieldsDefinitions().get("palletLabelRequired");
		Assert.assertNotNull(screenFieldDefinition);
		FieldTypeDefinition fieldTypeDefinition = screenFieldDefinition.getFieldTypeDefinition();
		Assert.assertNotNull(fieldTypeDefinition);
		Assert.assertTrue(BooleanFieldTypeDefinition.class.isAssignableFrom(fieldTypeDefinition.getClass()));
		BooleanFieldTypeDefinition booleanFieldTypeDefinition = (BooleanFieldTypeDefinition)screenFieldDefinition.getFieldTypeDefinition();
		Assert.assertEquals("Y", booleanFieldTypeDefinition.getTrueValue());
		Assert.assertEquals("N", booleanFieldTypeDefinition.getFalseValue());
	}

	@Test
	public void testFieldFromSuperClass() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(SignOn.class);
		Assert.assertNotNull(screenDefintion.getFieldsDefinitions().get("error"));
	}

	private ScreenEntityDefinition assertScreenExists(Class<?> clazz) {
		ScreenEntityDefinition screenDefintion = screenEntitiesRegistry.get(clazz);
		Assert.assertNotNull(screenDefintion);
		return screenDefintion;
	}
}
