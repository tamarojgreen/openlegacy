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

package org.openlegacy.db.definitions;

import org.openlegacy.db.actions.DbAction;

import java.io.Serializable;

public class SimpleDbActionDefinition implements DbActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private DbAction action;
	private String displayName = "";
	private boolean global = true;
	private String alias = "";
	private Class<?> targetEntity = void.class;

	public SimpleDbActionDefinition(DbAction action, String displayName) {
		this.action = action;
		this.displayName = displayName;
	}

	public DbAction getAction() {
		return action;
	}

	public void setAction(DbAction action) {
		this.action = action;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

}
