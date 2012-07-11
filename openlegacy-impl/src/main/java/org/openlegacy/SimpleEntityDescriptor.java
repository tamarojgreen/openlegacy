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
package org.openlegacy;

public class SimpleEntityDescriptor implements EntityDescriptor {

	private Class<?> entityClass;
	private String entityName;
	private String displayName;
	private boolean current;

	public SimpleEntityDescriptor(Class<?> entityClass, String entityName, String displayName) {
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.displayName = displayName;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}
}
