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
package org.openlegacy.designtime.terminal.analyzer.modules.table;

import org.apache.commons.lang.SystemUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableColumnFact implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TerminalField> fields;

	private ScreenEntityDesigntimeDefinition screenEntityDefinition;

	private List<TerminalField> headerFields = new ArrayList<TerminalField>();

	private boolean selectionField;

	public TableColumnFact(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields) {
		this.screenEntityDefinition = screenEntityDefinition;
		Collections.sort(fields, new FieldRowsComparator());
		this.fields = new ArrayList<TerminalField>();
		this.fields.addAll(fields);
	}

	public ScreenEntityDesigntimeDefinition getScreenEntityDefinition() {
		return screenEntityDefinition;
	}

	public List<TerminalField> getFields() {
		return fields;
	}

	public List<TerminalField> getHeaderFields() {
		return headerFields;
	}

	public int getStartColumn() {
		return fields.get(0).getPosition().getColumn();
	}

	public int getEndColumn() {
		return getStartColumn() + fields.get(0).getLength() - 1;
	}

	public int getStartRow() {
		return fields.get(0).getPosition().getRow();
	}

	public int getEndRow() {
		return fields.get(fields.size() - 1).getPosition().getRow();
	}

	private static class FieldRowsComparator implements Comparator<TerminalField> {

		public int compare(TerminalField o1, TerminalField o2) {
			return (o1.getPosition().getRow() - o2.getPosition().getRow());
		}
	}

	public boolean isSelectionField() {
		return selectionField;
	}

	public void setSelectionField(boolean selectionField) {
		this.selectionField = selectionField;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (TerminalField terminalField : fields) {
			sb.append(terminalField);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		return sb.toString();
	}
}
