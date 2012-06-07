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
		ScreenEntityDefinition screenDefinition = assertScreenExists(SignOn.class);
		ScreenIdentification screenIdentification = screenDefinition.getScreenIdentification();
		Assert.assertNotNull(screenIdentification);
		// 1 from class, 1 from super class
		Assert.assertEquals(2, screenIdentification.getScreenIdentifiers().size());
	}

	@Test
	public void testFields() {
		ScreenEntityDefinition screenDefinition = assertScreenExists(SignOn.class);
		// 4 from class, 1 from super class
		Assert.assertEquals(6, screenDefinition.getFieldsDefinitions().size());
	}

	@Test
	public void testScreenPart() {
		ScreenEntityDefinition screenDefinition = assertScreenExists(ItemDetails2.class);
		Assert.assertEquals(2, screenDefinition.getPartsDefinitions().size());
	}

	@Test
	public void testScreenTable() {
		ScreenEntityDefinition screenDefinition = assertScreenExists(ItemsList.class);
		Assert.assertEquals(1, screenDefinition.getTableDefinitions().size());
	}

	@Test
	public void testActions() {
		ScreenEntityDefinition screenDefintion = assertScreenExists(SignOn.class);
		// 1 from class, 1 from super class
		Assert.assertEquals(2, screenDefintion.getActions().size());
	}

	@Test
	public void testFieldTypes() {
		ScreenEntityDefinition screenDefinition = assertScreenExists(ItemDetails1.class);
		ScreenFieldDefinition screenFieldDefinition = screenDefinition.getFieldsDefinitions().get("palletLabelRequired");
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
		ScreenEntityDefinition screenDefinition = assertScreenExists(SignOn.class);
		Assert.assertNotNull(screenDefinition.getFieldsDefinitions().get("error"));
	}

	@Test
	public void testKeyFields() {
		ScreenEntityDefinition screenDefinition = assertScreenExists(ItemDetails1.class);
		Assert.assertNotNull(screenDefinition.getKeys());
		Assert.assertEquals("itemNumber", screenDefinition.getKeys().get(0).getName());
	}

	private ScreenEntityDefinition assertScreenExists(Class<?> clazz) {
		ScreenEntityDefinition screenDefinition = screenEntitiesRegistry.get(clazz);
		Assert.assertNotNull(screenDefinition);
		return screenDefinition;
	}
}
