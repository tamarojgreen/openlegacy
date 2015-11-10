/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.db.support.binders;

import org.openlegacy.db.DbEntity;
import org.openlegacy.db.DbEntityBinder;
import org.openlegacy.db.definitions.DbActionDefinition;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.definitions.ActionDefinition;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
public class DbEntityActionsBinder implements DbEntityBinder {

	@Inject
	private DbEntitiesRegistry dbEntitesRegistry;

	@Override
	public void populateEntity(Object entity) {
		if (entity instanceof List<?>) {
			List<?> entities = (List<?>) entity;
			for (Object object : entities) {
				calculateActions(object);
			}
		} else {
			calculateActions(entity);
		}
	}

	private void calculateActions(Object entity) {
		DbEntityDefinition entityDefinition = dbEntitesRegistry.get(entity.getClass());
		if (entityDefinition == null) {
			return;
		}
		List<ActionDefinition> actions = entityDefinition.getActions();

		List<DbActionDefinition> entityActions = new ArrayList<DbActionDefinition>();
		for (ActionDefinition actionDefinition : actions) {
			entityActions.add((DbActionDefinition) actionDefinition);
		}
		((DbEntity) entity).getActions().clear();
		((DbEntity) entity).getActions().addAll(entityActions);
	}
}
