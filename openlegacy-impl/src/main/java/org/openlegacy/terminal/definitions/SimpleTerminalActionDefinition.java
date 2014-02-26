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
package org.openlegacy.terminal.definitions;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.annotations.screen.Action.ActionType;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

import java.io.Serializable;

public class SimpleTerminalActionDefinition extends SimpleActionDefinition implements TerminalActionDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private TerminalPosition position;
	private AdditionalKey additionalKey;

	private String focusField;

	private ActionType type;

	private int length;

	private String when;

	private int sleep;

	public SimpleTerminalActionDefinition(SessionAction<? extends Session> action, AdditionalKey additionalKey,
			String displayName, TerminalPosition position) {
		super(action, displayName);
		this.position = position;
		this.additionalKey = additionalKey;
	}

	public SimpleTerminalActionDefinition(String actionName, String displayName, TerminalPosition position) {
		super(actionName, displayName);
		this.position = position;
	}
	
	public TerminalPosition getPosition() {
		return position;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
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

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	public int getRow() {
		return position.getRow();
	}

	public int getColumn() {
		return position.getColumn();
	}
}
