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

package org.openlegacy.db.mvc.rest;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.db.DbSession;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.mvc.AbstractRestController;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Ivan Bort
 */
public class AbstractDbRestController extends AbstractRestController {

	public static final String JSON = "application/json";
	public static final String XML = "application/xml";
	public static final String MODEL = "model";

	@Inject
	private DbSession dbSession;

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@Override
	protected Session getSession() {
		return dbSession;
	}

	@Override
	protected EntitiesRegistry<?, ?, ?> getEntitiesRegistry() {
		return dbEntitiesRegistry;
	}

	@Override
	protected List<ActionDefinition> getActions(Object entity) {
		// actions for screen exists on the entity. No need to fetch from registry
		return null;
	}

	@Override
	protected Object sendEntity(Object entity, String action) {
		return null;
	}

}
