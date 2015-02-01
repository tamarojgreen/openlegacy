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
package org.openlegacy.terminal.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.Arrays;
import java.util.List;

public class TerminalEqualsHashcodeUtil {

	public static boolean snapshotsEquals(TerminalSnapshot snapshot1, TerminalSnapshot snapshot2) {
		if (snapshot1.getRows().size() == 0 || snapshot2.getRows().size() == 0) {
			return false;
		}
		return snapshot1.getRows().containsAll(snapshot2.getRows());
	}

	public static int snapshotHashcode(TerminalSnapshot terminalSnapshot) {
		return Arrays.hashCode(terminalSnapshot.getRows().toArray()) + terminalSnapshot.getSnapshotType().hashCode();
	}

	public static boolean rowEquals(TerminalRow row1, TerminalRow row2) {
		return fieldsEquals(row1.getFields(), row2.getFields());
	}

	public static boolean fieldsEquals(List<TerminalField> fields1, List<TerminalField> fields2) {
		return Arrays.equals(fields1.toArray(), fields2.toArray());
	}

	public static int rowHashCode(TerminalRow row) {
		return Arrays.hashCode(row.getFields().toArray());
	}

	public static boolean fieldEquals(TerminalField field1, TerminalField field2) {
		return new EqualsBuilder().append(field1.getPosition(), field2.getPosition()).append(field1.getLength(),
				field2.getLength()).append(field1.isEditable(), field2.isEditable()).append(StringUtils.trim(field1.getValue()),
						StringUtils.trim(field2.getValue())).isEquals();
	}

	public static int fieldHashCode(TerminalField field) {
		return new HashCodeBuilder().append(field.getPosition()).append(field.isEditable()).append(field.getValue()).toHashCode();
	}
}
