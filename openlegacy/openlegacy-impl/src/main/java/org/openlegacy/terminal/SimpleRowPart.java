package org.openlegacy.terminal;

import org.openlegacy.terminal.support.SnapshotUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SimpleRowPart implements RowPart {

	private List<TerminalField> fields = new ArrayList<TerminalField>();
	private String value;

	public SimpleRowPart(TerminalField firstField) {
		fields.add(firstField);
	}

	public ScreenPosition getPosition() {
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
}
