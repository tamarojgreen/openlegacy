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
package org.openlegacy.terminal.persistance;

import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminalPersistedRow implements TerminalRow {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private int rowNumber;

	@XmlElement(name = "field", type = TerminalPersistedField.class)
	private List<TerminalField> fields = new ArrayList<TerminalField>();

	@XmlTransient
	private String value;

	@XmlTransient
	private int length;

	@Override
	public List<TerminalField> getFields() {
		initFieldsRow();
		return fields;
	}

	@Override
	public TerminalField getField(int column) {
		initFieldsRow();
		return SnapshotUtils.getField(this, column);
	}

	@Override
	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * To avoid replication between field row and it's parent row, TerminalPersistentField is not serializing row to XML. This
	 * function purpose is to fix the row of the field according to it's TerminalRow container set through
	 * <code>setRowNumber</code> method
	 */
	private void initFieldsRow() {
		// row was initialize
		if (fields.size() > 0 && fields.get(0).getPosition().getRow() > 0) {
			return;
		}
		// position row wasn't initialized, set with rowNumber
		for (TerminalField field : fields) {
			((TerminalPersistedField)field).setRow(rowNumber);
		}

	}

	/**
	 * accept the length externally. Cannot calculate by last field in the row since XML may be partial
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	@Override
	public boolean equals(Object obj) {
		initFieldsRow();
		if (!(obj instanceof TerminalRow)) {
			return false;
		}
		TerminalRow otherRow = (TerminalRow)obj;
		return TerminalEqualsHashcodeUtil.rowEquals(this, otherRow);
	}

	@Override
	public int hashCode() {
		initFieldsRow();
		return TerminalEqualsHashcodeUtil.rowHashCode(this);
	}

	@Override
	public String toString() {
		initFieldsRow();
		return SnapshotUtils.rowToString(this);
	}

	@Override
	public String getText() {
		if (value != null) {
			return value;
		}
		value = SnapshotUtils.getRowText(this);
		return value;
	}

	@Override
	public List<RowPart> getRowParts() {
		return SnapshotUtils.getRowParts(this);
	}

	@Override
	public String getText(int column, int length) {
		return SnapshotUtils.getRowText(this, column, length);
	}

}
