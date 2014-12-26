package org.openlegacy.db.definitions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.db.definitions.mock.DbDummyEntity;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@ContextConfiguration("/test-db-basic-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DbRegistryTest {

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@Test
	public void testRegistryLoad() {
		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(DbDummyEntity.class);
		Assert.assertNotNull(dbEntityDefinition);
		Assert.assertNotNull(dbEntityDefinition.getFieldsDefinitions().get("id"));

	}

	@Test
	public void testNavigation() {
		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(DbDummyEntity.class);
		Assert.assertNotNull(dbEntityDefinition);

		DbNavigationDefinition navigationDefinition = dbEntityDefinition.getNavigationDefinition();
		Assert.assertNotNull(navigationDefinition);
		Assert.assertEquals("dummyCategory", navigationDefinition.getCategory());
	}

	@Test
	public void testEntity() {
		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(DbDummyEntity.class);
		Assert.assertNotNull(dbEntityDefinition);

		Assert.assertEquals("DbDummyEntity", dbEntityDefinition.getEntityName());
		Assert.assertEquals("Dummy display name", dbEntityDefinition.getDisplayName());
		Assert.assertEquals(true, dbEntityDefinition.isChild());
		Assert.assertEquals(true, dbEntityDefinition.isWindow());
	}

	@Test
	public void testColumns() {
		DbEntityDefinition dbEntityDefinition = dbEntitiesRegistry.get(DbDummyEntity.class);
		Assert.assertNotNull(dbEntityDefinition);

		SimpleDbColumnFieldDefinition dbColumnDefinition = (SimpleDbColumnFieldDefinition)dbEntityDefinition.getColumnFieldsDefinitions().get(
				"description");
		Assert.assertNotNull(dbColumnDefinition);

		// @DbColumn
		Assert.assertEquals("Dummy db column name", dbColumnDefinition.getDisplayName());
		Assert.assertTrue(dbColumnDefinition.isPassword());
		Assert.assertEquals("Sample value", dbColumnDefinition.getSampleValue());
		Assert.assertEquals("Default value", dbColumnDefinition.getDefaultValue());
		Assert.assertEquals("Help text", dbColumnDefinition.getHelpText());
		Assert.assertTrue(dbColumnDefinition.isRightToLeft());
		Assert.assertTrue(dbColumnDefinition.isInternal());
		Assert.assertTrue(dbColumnDefinition.isMainDisplayField());
		// @Column
		Assert.assertEquals("description", dbColumnDefinition.getNameAttr());
		Assert.assertTrue(dbColumnDefinition.isUnique());
		Assert.assertFalse(dbColumnDefinition.isNullable());
		Assert.assertTrue(dbColumnDefinition.isInsertable());
		Assert.assertFalse(dbColumnDefinition.isUpdatable());
		Assert.assertEquals("VARCHAR(15)", dbColumnDefinition.getColumnDefinition());
		Assert.assertEquals(15, dbColumnDefinition.getLength());
		Assert.assertEquals(1, dbColumnDefinition.getPrecision());
		Assert.assertEquals(1, dbColumnDefinition.getScale());
	}
}
