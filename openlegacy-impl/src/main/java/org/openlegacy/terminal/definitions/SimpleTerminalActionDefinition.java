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

	private Class<?> targetEntity;

	// for design-time purposes
	private ScreenEntityDefinition targetEntityDefinition;

	private boolean defaultAction;

	public SimpleTerminalActionDefinition(SessionAction<? extends Session> action, AdditionalKey additionalKey,
			String displayName, TerminalPosition position) {
		super(action, displayName, additionalKey);
		this.position = position;
	}

	public TerminalPosition getPosition() {
		return position;
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
}
