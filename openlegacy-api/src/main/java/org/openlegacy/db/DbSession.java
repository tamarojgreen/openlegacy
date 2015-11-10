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

package org.openlegacy.db;

import org.openlegacy.Session;
import org.openlegacy.db.actions.DbAction;

public interface DbSession extends Session {

	public void login(String user, String password);

	public Object doAction(DbAction action, Object dbEntity, Object... keys);

	public void applyBinders(Object dbEntity);
}
