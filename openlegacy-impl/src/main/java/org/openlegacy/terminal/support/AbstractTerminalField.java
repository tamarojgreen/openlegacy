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
package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.exceptions.TerminalActionException;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;
import org.openlegacy.utils.StringUtil;

public abstract class AbstractTerminalField implements ModifiableTerminalField, Cloneable {

	private static final long serialVersionUID = 1L;
	private String value;
	private String modifiedValue;

	private TerminalPosition position;
	private TerminalPosition endPosition;
	private Integer length;

	public String getValue() {
		if (getModifiedValue() != null) {
			return getModifiedValue();
		}
		return getOriginalValue();
	}

	public String getOriginalValue() {
		if (value == null) {
			value = initValue();
		}
		return value;
	}

	public void setValue(String value, boolean modified) {
		if (modified) {
			modifiedValue = value;
		} else {
			this.value = value;
		}
	}

	public int getLength() {
		if (length == null) {
			length = initLength();
		}
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPosition(TerminalPosition position) {
		this.position = position;
	}

	public void setEndPosition(TerminalPosition endPosition) {
		this.endPosition = endPosition;
	}

	public TerminalPosition getPosition() {
		if (position == null) {
			position = initPosition();
		}
		return position;
	}

	public TerminalPosition getEndPosition() {
		if (endPosition == null) {
			endPosition = initEndPosition();
		}
		return endPosition;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalField)) {
			return false;
		}
		TerminalField otherField = (TerminalField)obj;
		return TerminalEqualsHashcodeUtil.fieldEquals(this, otherField);
	}

	@Override
	public int hashCode() {
		return TerminalEqualsHashcodeUtil.fieldHashCode(this);
	}

	@Override
	public String toString() {
		return SnapshotUtils.fieldToString(this);
	}

	public boolean isEmpty() {
		return StringUtil.isEmpty(getValue());
	}

	public boolean isPassword() {
		return isEditable() && isHidden();
	}

	public void setValue(String value) {
		if (isEditable()) {
			modifiedValue = value;
		} else {
			throw (new TerminalActionException("An attempt to update a readonly field:" + this));
		}
	}

	public boolean isModified() {
		return modifiedValue != null;
	}

	public String getModifiedValue() {
		return modifiedValue;
	}

	@Override
	public abstract TerminalField clone();

	protected abstract int initLength();

	protected abstract String initValue();

	protected abstract TerminalPosition initPosition();

	protected abstract TerminalPosition initEndPosition();

	public boolean isUppercase() {
		return false;
	}

	public boolean isMultyLine() {
		if (getEndPosition() == null) {
			return false;
		}
		return getEndPosition().getRow() > getPosition().getRow();
	}

	public int compareTo(TerminalField other) {
		return getPosition().compareTo(other.getPosition());
	}
}
