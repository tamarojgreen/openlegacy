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
package org.openlegacy.terminal.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

import java.io.Serializable;

public class SimpleTerminalActionDefinition extends SimpleActionDefinition implements TerminalActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalPosition position;
	private AdditionalKey additionalKey;

	private Class<?> targetEntity;

	// for design-time purposes
	private ScreenEntityDefinition targetEntityDefinition;

	private boolean defaultAction;

	private String focusField;

	public SimpleTerminalActionDefinition(SessionAction<? extends Session> action, AdditionalKey additionalKey,
			String displayName, TerminalPosition position) {
		super(action, displayName);
		this.position = position;
		this.additionalKey = additionalKey;
	}

	public SimpleTerminalActionDefinition(String actionName, String displayName) {
		super(actionName, displayName);
	}

	public TerminalPosition getPosition() {
		return position;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	@Override
	public boolean isDefaultAction() {
		return defaultAction;
	}

	public void setDefault(boolean defaultAction) {
		this.defaultAction = defaultAction;
	}

	public ScreenEntityDefinition getTargetEntityDefinition() {
		return targetEntityDefinition;
	}

	public void setTargetEntityDefinition(ScreenEntityDefinition targetEntityDefinition) {
		this.targetEntityDefinition = targetEntityDefinition;
	}

	public String getFocusField() {
		return focusField;
	}

	public void setFocusField(String focusField) {
		this.focusField = focusField;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}
}
