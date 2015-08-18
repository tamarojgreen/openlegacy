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

package org.openlegacy.modules.roles;

import org.openlegacy.modules.SessionModule;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.ScreenEntity;

/**
 * @author Ivan Bort
 */
public interface Roles extends SessionModule {

	public boolean isEntityPermitted(Class<?> entityClass, String[] userRoles);

	public boolean isEntityPermitted(String entityName, String[] userRoles);

	public boolean isActionPermitted(ScreenEntity entity);

	public void populateEntity(Object entity, Login loginModule);
}
