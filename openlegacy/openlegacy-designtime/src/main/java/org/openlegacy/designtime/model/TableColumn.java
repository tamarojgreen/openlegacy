package org.openlegacy.designtime.model;

import org.openlegacy.terminal.TerminalField;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableColumn implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TerminalField> fields;

	private ScreenEntityDesigntimeDefinition screenEntityDefinition;

	public TableColumn(ScreenEntityDesigntimeDefinition screenEntityDefinition, List<TerminalField> fields) {
		this.screenEntityDefinition = screenEntityDefinition;
		Collections.sort(fields, new FieldRowsComparator());
		this.fields = fields;
	}

	public ScreenEntityDesigntimeDefinition getScreenEntityDefinition() {
		return screenEntityDefinition;
	}

	public List<TerminalField> getFields() {
		return fields;
	}

	private static class FieldRowsComparator implements Comparator<TerminalField> {

		public int compare(TerminalField o1, TerminalField o2) {
			return (o1.getPosition().getRow() - o2.getPosition().getRow());
		}

	}
}
