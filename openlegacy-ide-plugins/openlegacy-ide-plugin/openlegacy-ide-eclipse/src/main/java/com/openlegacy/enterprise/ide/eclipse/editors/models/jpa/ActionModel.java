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

package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.annotations.db.Action;
import org.openlegacy.db.definitions.DbActionDefinition;
import org.openlegacy.utils.StringUtil;

/**
 * @author Ivan Bort
 * 
 */
public class ActionModel extends JpaNamedObject implements DbActionDefinition {

	private static final Class<?> DEFAULT_TARGET_ENTITY = void.class;

	// annotation attributes
	private SessionAction<? extends Session> action;
	private String displayName = "";
	private boolean global = true;
	private String alias = "";
	private Class<?> targetEntity = DEFAULT_TARGET_ENTITY;
	private String targetEntityClassName = DEFAULT_TARGET_ENTITY.getSimpleName();

	// other
	private String actionName;
	private String prevActionName;
	private String prevDisplayName;
	private String prevAlias;
	private boolean prevGlobal;
	private String prevTargetEntityClassName;

	public ActionModel(String actionName, String displayName, boolean global, String alias, String targetEntityClassName) {
		super(Action.class.getSimpleName());

		this.actionName = actionName;
		this.displayName = displayName;
		this.global = global;
		this.alias = alias;
		if (!StringUtils.isEmpty(targetEntityClassName)) {
			this.targetEntityClassName = targetEntityClassName;
		}

		this.prevActionName = actionName;
		this.prevDisplayName = (displayName == null) ? StringUtil.toDisplayName(actionName.toLowerCase()) : displayName;
		this.prevAlias = (alias == null) ? actionName.toLowerCase() : alias;
		this.prevGlobal = global;
		if (!StringUtils.isEmpty(targetEntityClassName)) {
			this.prevTargetEntityClassName = targetEntityClassName;
		}
	}

	public ActionModel cloneModel() {
		return new ActionModel(actionName, displayName, global, alias, targetEntityClassName);
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
		return displayName;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public boolean isDefaultAction() {
		return false;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

	@Override
	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	@Override
	public int getRow() {
		return 0;
	}

	@Override
	public int getColumn() {
		return 0;
	}

	@Override
	public int getLength() {
		return 0;
	}

	@Override
	public String getWhen() {
		return null;
	}

	public Class<?> getDefaultTargetEntity() {
		return DEFAULT_TARGET_ENTITY;
	}

	public String getDefaultDisplayName() {
		return StringUtils.isEmpty(actionName) ? "" : StringUtil.toDisplayName(actionName.toLowerCase());
	}

	public String getDefaultAlias() {
		return StringUtils.isEmpty(actionName) ? "" : actionName.toLowerCase();
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public String getPrevActionName() {
		return prevActionName;
	}

	public String getPrevDisplayName() {
		return prevDisplayName;
	}

	public String getPrevAlias() {
		return prevAlias;
	}

	public boolean isPrevGlobal() {
		return prevGlobal;
	}

	public String getPrevTargetEntityClassName() {
		return prevTargetEntityClassName;
	}

	public void setAction(SessionAction<? extends Session> action) {
		this.action = action;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	public void setTargetEntityDefaultValue() {
		targetEntityClassName = DEFAULT_TARGET_ENTITY.getSimpleName();
		targetEntity = DEFAULT_TARGET_ENTITY;
	}

}
