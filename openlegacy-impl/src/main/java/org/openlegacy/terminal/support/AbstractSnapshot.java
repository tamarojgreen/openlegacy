package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotTextRenderer;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;
import org.openlegacy.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractSnapshot implements TerminalSnapshot, Serializable {

	private static final long serialVersionUID = 1L;

	private List<TerminalRow> rows;
	private List<TerminalField> fields;
	private List<TerminalField> logicalFields;
	private String text;

	private List<TerminalPosition> fieldSeperators;

	private ScreenSize screenSize;

	private TerminalPosition cursor;

	@Override
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TerminalSnapshotTextRenderer.instance().render(this, baos);
		return StringUtil.toString(baos);
	}

	@Override
	public int hashCode() {
		return TerminalEqualsHashcodeUtil.snapshotHashcode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalSnapshot)) {
			return false;
		}
		return TerminalEqualsHashcodeUtil.snapshotsEquals(this, (TerminalSnapshot)obj);
	}

	public List<TerminalPosition> getFieldSeperators() {
		if (fieldSeperators == null) {
			fieldSeperators = initFieldSeperators();
		}
		return fieldSeperators;
	}

	protected abstract List<TerminalPosition> initFieldSeperators();

	public ScreenSize getSize() {
		if (screenSize == null) {
			screenSize = initScreenSize();
		}
		return screenSize;
	}

	protected abstract ScreenSize initScreenSize();

	public TerminalRow getRow(int rowNumber) {
		List<TerminalRow> rows = getRows();
		for (TerminalRow terminalRow : rows) {
			if (terminalRow.getRowNumber() == rowNumber) {
				return terminalRow;
			}
		}
		return null;
	}

	public TerminalPosition getCursorPosition() {
		if (cursor == null) {
			cursor = initCursorPosition();
		}
		return cursor;
	}

	protected abstract TerminalPosition initCursorPosition();

	public List<TerminalRow> getRows() {
		if (rows != null) {
			return rows;
		}

		rows = new ArrayList<TerminalRow>();
		for (int i = 1; i <= getSize().getRows(); i++) {
			rows.add(new SimpleTerminalRow(i));
		}

		Collection<TerminalField> fields = getFields();
		for (TerminalField field : fields) {
			try {
				TerminalRow row = rows.get(field.getPosition().getRow() - 1);
				row.getFields().add(field);
			} catch (IndexOutOfBoundsException e) {
				throw (e);
			}
		}
		return rows;
	}

	public String getText(TerminalPosition position, int length) {
		return SnapshotUtils.getText(this, position, length);
	}

	public String getText() {
		if (text != null) {
			return text;
		}
		text = initText();
		return text;
	}

	protected abstract String initText();

	public List<TerminalField> getFields() {
		if (fields == null) {
			fields = initFields();
		}

		return fields;
	}

	public List<TerminalField> getLogicalFields() {
		if (logicalFields == null) {
			logicalFields = initLogicalFields();
		}

		return fields;
	}

	protected abstract List<TerminalField> initFields();

	protected abstract List<TerminalField> initLogicalFields();

	public TerminalField getField(TerminalPosition position) {
		TerminalField field = SnapshotUtils.getField(this, position);
		return field;
	}

	public SnapshotType getSnapshotType() {
		return SnapshotType.INCOMING;
	}
}
