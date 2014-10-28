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

import org.openlegacy.SessionAction;
import org.openlegacy.db.DbSession;

import javax.persistence.EntityManager;

public interface DbAction extends SessionAction<DbSession> {

	<T> T perform(EntityManager entityManager, T entity, Object... keys);

}
