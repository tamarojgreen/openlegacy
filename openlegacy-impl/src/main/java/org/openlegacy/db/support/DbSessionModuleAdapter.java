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

package org.openlegacy.db.support;

import org.openlegacy.db.DbSession;
import org.openlegacy.db.DbSessionModule;
import org.openlegacy.support.SessionModuleAdapter;

/**
 * @author Ivan Bort
 */
public class DbSessionModuleAdapter extends SessionModuleAdapter<DbSession> implements DbSessionModule {

	private static final long serialVersionUID = 1L;

	/**
	 * for serialization purpose only
	 */
	public DbSessionModuleAdapter() {}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
