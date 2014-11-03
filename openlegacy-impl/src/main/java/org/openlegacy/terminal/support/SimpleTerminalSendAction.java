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
package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSendAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleTerminalSendAction implements TerminalSendAction, Serializable {

	private static final long serialVersionUID = 1L;

	private List<TerminalField> modifiedFields = new ArrayList<TerminalField>();
	private Object command;
	private TerminalPosition cursorPosition;

	private int sleep = 0;

	public SimpleTerminalSendAction(Object command) {
		this.command = command;
	}

	@Override
	public List<TerminalField> getFields() {
		return modifiedFields;
	}

	@Override
	public Object getCommand() {
		return command;
	}

	@Override
	public TerminalPosition getCursorPosition() {
		return cursorPosition;
	}

	@Override
	public void setCursorPosition(TerminalPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
