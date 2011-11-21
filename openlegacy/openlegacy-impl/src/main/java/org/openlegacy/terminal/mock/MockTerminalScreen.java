package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.ScreenUtils;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.openlegacy.terminal.utils.FieldsQuery;
import org.openlegacy.terminal.utils.FieldsQuery.AllFieldsCriteria;
import org.openlegacy.terminal.utils.FieldsQuery.EditableFieldsCriteria;
import org.openlegacy.terminal.utils.ScreenPainter;
import org.openlegacy.terminal.utils.TerminalEqualsHashcodeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A mock screen is conducted from a persisted snapshot, and reconstruct the entire screen data from the persisted fields
 * 
 */
public class MockTerminalScreen implements TerminalScreen {

	private TerminalSnapshot terminalSnapshot;

	private String screenText;

	private ArrayList<ScreenPosition> attributes;

	public MockTerminalScreen(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
	}

	public ScreenSize getSize() {
		return terminalSnapshot.getSize();
	}

	public List<TerminalRow> getRows() {
		return terminalSnapshot.getRows();
	}

	public SnapshotType getSnapshotType() {
		return terminalSnapshot.getSnapshotType();
	}

	public String getText() {
		initContent();
		return screenText;
	}

	public String getText(ScreenPosition position, int length) {
		initContent();
		int beginIndex = ScreenUtils.toAbsolutePosition(position, getSize());
		return screenText.substring(beginIndex, beginIndex + length);
	}

	public TerminalField getField(ScreenPosition position) {
		TerminalRow row = terminalSnapshot.getRows().get(position.getRow() - 1);
		return row.getField(position.getColumn());
	}

	public Collection<TerminalField> getFields() {
		initContent();
		return FieldsQuery.queryFields(terminalSnapshot, AllFieldsCriteria.instance());
	}

	public Collection<TerminalField> getEditableFields() {
		initContent();
		return FieldsQuery.queryFields(terminalSnapshot, EditableFieldsCriteria.instance());
	}

	private void initContent() {

		if (screenText == null) {

			ScreenSize size = getSize();
			StringBuilder buffer = ScreenUtils.initEmptyBuffer(size);

			attributes = new ArrayList<ScreenPosition>();

			List<TerminalRow> rows = terminalSnapshot.getRows();

			for (TerminalRow terminalRow : rows) {
				List<TerminalField> rowFields = terminalRow.getFields();
				for (TerminalField terminalField : rowFields) {
					ScreenUtils.placeContentOnBuffer(buffer, terminalField, size);

					ScreenPosition fieldPosition = terminalField.getPosition();
					attributes.add(new SimpleScreenPosition(fieldPosition.getRow(), fieldPosition.getColumn() - 1));
				}
			}
			screenText = buffer.toString();
		}
	}

	public Object getDelegate() {
		throw (new UnsupportedOperationException("Mock terminal screen has not deleegate"));
	}

	@Override
	public String toString() {
		return ScreenPainter.paint(this, true);
	}

	public List<ScreenPosition> getFieldSeperators() {
		initContent();
		return attributes;
	}

	public ScreenPosition getCursorPosition() {
		return terminalSnapshot.getCursorPosition();
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

}
