package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.ScreenUtils;
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
public class MockTerminalScreen implements TerminalSnapshot {

	private TerminalSnapshot terminalSnapshot;

	private String screenText;

	private ArrayList<ScreenPosition> fieldSeperators;

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
		return ScreenUtils.getText(screenText, getSize(), position, length);
	}

	public TerminalField getField(ScreenPosition position) {
		return ScreenUtils.getField(terminalSnapshot.getRows(), position);
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
			fieldSeperators = new ArrayList<ScreenPosition>();
			screenText = ScreenUtils.initSnapshot(getRows(), getSize(), fieldSeperators);
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
		return fieldSeperators;
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
