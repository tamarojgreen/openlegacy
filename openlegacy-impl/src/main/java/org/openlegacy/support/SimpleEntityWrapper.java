/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.support;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.EntityWrapper;

import java.util.List;

public class SimpleEntityWrapper implements EntityWrapper {

	private Object entity;

	private String entityName;

	private List<EntityDescriptor> paths;

	public SimpleEntityWrapper() {
		// for de-serialization
	}

	public SimpleEntityWrapper(Object entity, List<EntityDescriptor> paths) {
		this.entity = entity;

		this.entityName = entity.getClass().getSimpleName();
		this.paths = paths;
	}

	public Object getEntity() {
		return entity;
	}

	public String getEntityName() {
		return entityName;
	}

	public List<EntityDescriptor> getPaths() {
		return paths;
	}
}
