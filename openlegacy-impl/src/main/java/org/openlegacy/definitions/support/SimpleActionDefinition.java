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
package org.openlegacy.definitions.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions.SimpleDrilldownAction;
import org.openlegacy.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

public class SimpleActionDefinition implements ActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String displayName;
	private transient SessionAction<? extends Session> action;
	private AdditionalKey additionalKey;
	private String alias;
	private String actionName;

	private boolean defaultAction;

	private String targetEntityName;

	private boolean global;

	private Class<?> targetEntity;

	private int row = 0;
	private int column = 0;
	private int length = 0;
	private String when = ".*";

	private boolean rolesRequired = false;
	private List<String> roles;

	// for design-time purposes
	private EntityDefinition<?> targetEntityDefinition;

	/**
	 * for serialization purpose only
	 */
	public SimpleActionDefinition() {}

	/**
	 * Used for run-time registry
	 * 
	 * @param action
	 * @param displayName
	 */
	public SimpleActionDefinition(SessionAction<? extends Session> action, String displayName) {
		this.action = action;
		if (action instanceof SimpleDrilldownAction) {
			this.actionName = ((SimpleDrilldownAction) action).getAction().getClass().getSimpleName();
		} else {
			this.actionName = action.getClass().getSimpleName();
		}
		this.displayName = displayName;
	}

	/**
	 * Used for design-time code generation
	 * 
	 * @param actionName
	 * @param displayName
	 */
	public SimpleActionDefinition(String actionName, String displayName) {
		this.actionName = actionName;
		this.displayName = displayName;
	}

	@Override
	public SessionAction<? extends Session> getAction() {
		return action;
	}

	@Override
	public String getActionName() {
		return actionName;
	}

	@Override
	public String getDisplayName() {
		if (displayName == null) {
			displayName = StringUtil.toDisplayName(getActionName().toLowerCase());
		}
		return displayName;
	}

	@Override
	public String getAlias() {
		if (alias == null) {
			alias = getActionName().toLowerCase();
		}
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setAction(SessionAction<? extends Session> action) {
		this.action = action;
	}

	@Override
	public boolean isDefaultAction() {
		return defaultAction;
	}

	public void setDefaultAction(boolean defaultAction) {
		this.defaultAction = defaultAction;
	}

	public String getTargetEntityName() {
		return targetEntityName;
	}

	public void setTargetEntityName(String targetEntityName) {
		this.targetEntityName = targetEntityName;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public EntityDefinition<?> getTargetEntityDefinition() {
		return targetEntityDefinition;
	}

	public void setTargetEntityDefinition(EntityDefinition<?> targetEntityDefinition) {
		this.targetEntityDefinition = targetEntityDefinition;
	}

	@Override
	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public String getWhen() {
		return when;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}

	public boolean isRolesRequired() {
		return rolesRequired;
	}

	public void setRolesRequired(boolean rolesRequired) {
		this.rolesRequired = rolesRequired;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
