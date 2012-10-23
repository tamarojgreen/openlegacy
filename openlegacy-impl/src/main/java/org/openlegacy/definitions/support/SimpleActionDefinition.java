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
package org.openlegacy.definitions.support;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.ActionDefinition;

import java.io.Serializable;

public class SimpleActionDefinition implements ActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String displayName;
	private transient SessionAction<? extends Session> action;
	private String alias;
	private String actionName;

	private boolean defaultAction;

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
		this.actionName = action.getClass().getSimpleName();
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

	public SessionAction<? extends Session> getAction() {
		return action;
	}

	public String getActionName() {
		return actionName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setAction(SessionAction<? extends Session> action) {
		this.action = action;
	}

	public boolean isDefaultAction() {
		return defaultAction;
	}

	public void setDefaultAction(boolean defaultAction) {
		this.defaultAction = defaultAction;
	}
}
