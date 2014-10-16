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
package org.openlegacy.db.support;

import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.DbPartEntityDefinition;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.support.AbstractEntitiesRegistry;

public class DefaultDbEntitiesRegistry extends AbstractEntitiesRegistry<DbEntityDefinition, DbFieldDefinition, DbPartEntityDefinition> implements DbEntitiesRegistry {

	private static final long serialVersionUID = 1L;

}
