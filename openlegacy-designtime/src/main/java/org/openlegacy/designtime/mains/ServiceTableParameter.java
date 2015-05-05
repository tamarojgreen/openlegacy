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
package org.openlegacy.designtime.mains;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.TableDefinition;

/**
 * @author Aleksey Yeremeyev
 * 
 */
public class ServiceTableParameter implements ServiceParameter {
	private EntityDefinition<?> entityDefinition;
	private TableDefinition<?> tableDefinition;

	public ServiceTableParameter(EntityDefinition<?> entityDefinition, TableDefinition<?> tableDefinition) {
		this.entityDefinition = entityDefinition;
		this.tableDefinition = tableDefinition;
	}

	public EntityDefinition<?> getEntityDefinition() {
		return entityDefinition;
	}

	public TableDefinition<?> getTableDefinition() {
		return tableDefinition;
	}
}
