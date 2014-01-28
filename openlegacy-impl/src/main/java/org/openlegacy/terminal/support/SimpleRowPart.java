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

import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SimpleRowPart implements RowPart {

	private List<TerminalField> fields = new ArrayList<TerminalField>();
	private String value;

	public SimpleRowPart(TerminalField firstField) {
		fields.add(firstField);
	}

	public TerminalPosition getPosition() {
		return getFirstField().getPosition();
	}

	private TerminalField getFirstField() {
		return fields.get(0);
	}

	private TerminalField getLastField() {
		return fields.get(fields.size() - 1);
	}

	public String getValue() {
		if (value != null) {
			return value;
		}
		StringBuilder rowContent = SnapshotUtils.initEmptyBuffer(getLength());
		for (TerminalField terminalField : fields) {
			int startPosition = terminalField.getPosition().getColumn() - getFirstField().getPosition().getColumn();
			SnapshotUtils.placeContentOnBuffer(rowContent, startPosition, terminalField.getValue());
		}
		value = rowContent.toString();
		return value;
	}

	public void appendField(TerminalField field) {
		fields.add(field);
	}

	public int getLength() {
		return getLastField().getPosition().getColumn() + getLastField().getLength() - getFirstField().getPosition().getColumn();
	}

	public boolean isEditable() {
		return getFirstField().isEditable();
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0}:{1}", getPosition().toString(), getValue());
	}

	public List<TerminalField> getFields() {
		return fields;
	}
}
