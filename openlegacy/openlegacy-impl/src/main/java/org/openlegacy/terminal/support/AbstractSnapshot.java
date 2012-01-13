package org.openlegacy.terminal.support;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotTextRenderer;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractSnapshot implements TerminalSnapshot, Serializable {

	private static final long serialVersionUID = 1L;

	private List<TerminalRow> rows;
	private List<TerminalField> fields;
	protected String text;

	protected ScreenSize screenSize;

	protected TerminalPosition cursor;

	@Override
	public String toString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TerminalSnapshotTextRenderer.instance().render(this, baos);
		return new String(baos.toByteArray());
	}

	@Override
	public int hashCode() {
		// return HashCodeBuilder.reflectionHashCode(this);
		return TerminalEqualsHashcodeUtil.snapshotHashcode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TerminalSnapshot)) {
			return false;
		}
		return TerminalEqualsHashcodeUtil.snapshotsEquals(this, (TerminalSnapshot)obj);
	}

	public TerminalRow getRow(int rowNumber) {
		List<TerminalRow> rows = getRows();
		for (TerminalRow terminalRow : rows) {
			if (terminalRow.getRowNumber() == rowNumber) {
				return terminalRow;
			}
		}
		return null;
	}

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
			TerminalRow row = rows.get(field.getPosition().getRow() - 1);
			row.getFields().add(field);
		}
		return rows;
	}

	public String getText(TerminalPosition position, int length) {
		return SnapshotUtils.getText(this, position, length);
	}

	public List<TerminalField> getFields() {
		if (fields != null) {
			return fields;
		}

		fields = buildAllFields();
		return fields;
	}

	protected abstract List<TerminalField> buildAllFields();

	public TerminalField getField(TerminalPosition position) {
		TerminalField field = SnapshotUtils.getField(this, position);
		return field;
	}

	public SnapshotType getSnapshotType() {
		return SnapshotType.INCOMING;
	}
}
