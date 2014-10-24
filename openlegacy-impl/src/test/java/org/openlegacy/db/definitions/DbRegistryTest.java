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
	public void testActions() {
		DbEntityDefinition entityDefinition = assertEntityExists(DbDummyEntity.class);
		entityDefinition.getActions().size();
	}

	private DbEntityDefinition assertEntityExists(Class<?> clazz) {
		DbEntityDefinition entityDefinition = dbEntitiesRegistry.get(clazz);
		Assert.assertNotNull(entityDefinition);
		return entityDefinition;
	}
}
