package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.ScreenUtils;
import org.openlegacy.terminal.support.SimpleScreenPosition;
import org.openlegacy.terminal.utils.ScreenPainter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A mock screen is conducted from a persisted screen, and reconstruct the entire screen data from the persisted fields
 * 
 */
public class MockTerminalScreen implements TerminalScreen {

	private TerminalSnapshot terminalSnapshot;

	private List<TerminalField> allFields;
	private List<TerminalField> editableFields;

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

	public Collection<TerminalField> getEditableFields() {
		initContent();
		return editableFields;
	}

	private void initContent() {

		if (allFields == null) {

			ScreenSize size = getSize();
			StringBuilder buffer = ScreenUtils.initEmptyBuffer(size);

			allFields = new ArrayList<TerminalField>();
			editableFields = new ArrayList<TerminalField>();
			attributes = new ArrayList<ScreenPosition>();

			List<TerminalRow> rows = terminalSnapshot.getRows();

			for (TerminalRow terminalRow : rows) {
				List<TerminalField> rowFields = terminalRow.getFields();
				for (TerminalField terminalField : rowFields) {
					ScreenUtils.placeContentOnBuffer(buffer, terminalField, size);
					allFields.add(terminalField);
					if (terminalField.isEditable()) {
						editableFields.add(terminalField);
					}

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
}
