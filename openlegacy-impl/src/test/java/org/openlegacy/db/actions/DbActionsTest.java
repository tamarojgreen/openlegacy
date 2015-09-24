/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.db.actions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.db.DbSession;
import org.openlegacy.db.actions.DbActions.CREATE;
import org.openlegacy.db.actions.DbActions.DELETE;
import org.openlegacy.db.actions.DbActions.READ;
import org.openlegacy.db.actions.DbActions.UPDATE;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.mock.DbDummyEntity;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.definitions.ActionDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ivan Bort
 * 
 */
@ContextConfiguration("/test-db-basic-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DbActionsTest {

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@Inject
	private ApplicationContext applicationContext;

	@PersistenceContext
	transient EntityManager entityManager;

	@Test
	public void testActions() {
		DbEntityDefinition entityDefinition = assertEntityExists(DbDummyEntity.class);
		Assert.assertEquals(5, entityDefinition.getActions().size());
	}

	@Test
	public void testReadAction() {
		DbDummyEntity dummyEntity = new DbDummyEntity();
		dummyEntity.setId(1);

		DbSession dbSession = applicationContext.getBean(DbSession.class);
		DbEntityDefinition entityDefinition = assertEntityExists(DbDummyEntity.class);
		Object returnedObject = null;

		List<ActionDefinition> actions = entityDefinition.getActions();
		for (ActionDefinition actionDefinition : actions) {
			if (actionDefinition.getActionName().equals(READ.class.getSimpleName())) {
				returnedObject = dbSession.doAction((DbAction) actionDefinition.getAction(), dummyEntity, dummyEntity.getId());
				break;
			}
		}

		Assert.assertNotNull(returnedObject);
		Assert.assertTrue(returnedObject instanceof DbDummyEntity);
		DbDummyEntity entity = (DbDummyEntity) returnedObject;
		Assert.assertEquals(1, entity.getId().intValue());
		Assert.assertEquals("description", entity.getDescription());
	}

	@Transactional
	@Test
	public void testCreateAction() {
		DbDummyEntity dummyEntity = new DbDummyEntity();
		dummyEntity.setId(2);
		dummyEntity.setDescription("description 2");

		DbSession dbSession = applicationContext.getBean(DbSession.class);
		DbEntityDefinition entityDefinition = assertEntityExists(DbDummyEntity.class);
		Object returnedObject = null;

		List<ActionDefinition> actions = entityDefinition.getActions();
		for (ActionDefinition actionDefinition : actions) {
			if (actionDefinition.getActionName().equals(CREATE.class.getSimpleName())) {
				returnedObject = dbSession.doAction((DbAction) actionDefinition.getAction(), dummyEntity);
				break;
			}
		}
		Assert.assertNotNull(returnedObject);
		Assert.assertTrue(returnedObject instanceof DbDummyEntity);
		DbDummyEntity entity = (DbDummyEntity) returnedObject;
		Assert.assertEquals(2, entity.getId().intValue());
		Assert.assertEquals("description 2", entity.getDescription());

		List<DbDummyEntity> resultList = entityManager.createQuery("SELECT e FROM DbDummyEntity e", DbDummyEntity.class).getResultList();
		Assert.assertEquals(2, resultList.size());
	}

	@Transactional
	@Test
	public void testUpdateAction() {
		DbDummyEntity dummyEntity = new DbDummyEntity();
		dummyEntity.setId(1);
		dummyEntity.setDescription("changed description");

		DbSession dbSession = applicationContext.getBean(DbSession.class);
		DbEntityDefinition entityDefinition = assertEntityExists(DbDummyEntity.class);
		Object returnedObject = null;

		List<ActionDefinition> actions = entityDefinition.getActions();
		for (ActionDefinition actionDefinition : actions) {
			if (actionDefinition.getActionName().equals(UPDATE.class.getSimpleName())) {
				returnedObject = dbSession.doAction((DbAction) actionDefinition.getAction(), dummyEntity, dummyEntity.getId());
				break;
			}
		}

		Assert.assertNotNull(returnedObject);
		Assert.assertTrue(returnedObject instanceof DbDummyEntity);
		DbDummyEntity entity = (DbDummyEntity) returnedObject;
		Assert.assertEquals(1, entity.getId().intValue());
		Assert.assertEquals("changed description", entity.getDescription());
		DbDummyEntity mergedEntity = entityManager.find(DbDummyEntity.class, entity.getId());
		Assert.assertNotNull(mergedEntity);
		Assert.assertEquals(entity.getId(), mergedEntity.getId());
		Assert.assertEquals(entity.getDescription(), mergedEntity.getDescription());
	}

	@Transactional
	@Test
	public void testDeleteAction() {
		DbDummyEntity dummyEntity = new DbDummyEntity();
		dummyEntity.setId(1);

		DbSession dbSession = applicationContext.getBean(DbSession.class);
		// get entity
		DbDummyEntity fromDb = (DbDummyEntity) dbSession.doAction(DbActions.READ(), dummyEntity, dummyEntity.getId());
		Assert.assertNotNull(fromDb);

		DbEntityDefinition entityDefinition = assertEntityExists(DbDummyEntity.class);

		List<ActionDefinition> actions = entityDefinition.getActions();
		for (ActionDefinition actionDefinition : actions) {
			if (actionDefinition.getActionName().equals(DELETE.class.getSimpleName())) {
				dbSession.doAction((DbAction) actionDefinition.getAction(), fromDb);
				break;
			}
		}

		List<DbDummyEntity> resultList = entityManager.createQuery("SELECT e FROM DbDummyEntity e", DbDummyEntity.class).getResultList();
		Assert.assertEquals(0, resultList.size());
	}

	private DbEntityDefinition assertEntityExists(Class<?> clazz) {
		DbEntityDefinition entityDefinition = dbEntitiesRegistry.get(clazz);
		Assert.assertNotNull(entityDefinition);
		return entityDefinition;
	}

}
