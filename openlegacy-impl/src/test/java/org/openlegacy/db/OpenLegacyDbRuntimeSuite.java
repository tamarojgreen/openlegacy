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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openlegacy.db.actions.DbActionsTest;
import org.openlegacy.db.definitions.DbRegistryTest;
import org.openlegacy.db.modules.menu.DefaultDbMenuModelTest;
import org.openlegacy.db.modules.roles.DefaultDbRolesModuleTest;

@RunWith(Suite.class)
@SuiteClasses({ DbRegistryTest.class, DefaultDbMenuModelTest.class, DbActionsTest.class, DefaultDbRolesModuleTest.class })
public class OpenLegacyDbRuntimeSuite {

}
