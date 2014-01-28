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
package org.openlegacy;

import java.io.Serializable;

public class SimpleEntityDescriptor implements EntityDescriptor, Serializable {

	private static final long serialVersionUID = 1L;

	private transient Class<?> entityClass;
	private String entityName;
	private String displayName;
	private boolean current;

	private boolean requiresParameters;

	public SimpleEntityDescriptor(Class<?> entityClass, String entityName, String displayName, boolean requiresParameters) {
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.displayName = displayName;
		this.requiresParameters = requiresParameters;
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

	public boolean isRequiresParameters() {
		return requiresParameters;
	}
	
	public void setRequiresParameters(boolean requiresParameters) {
		this.requiresParameters = requiresParameters;
	}
}
