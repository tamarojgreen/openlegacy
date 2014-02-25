package org.openlegacy.providers.h3270;

import org.apache.commons.lang.StringUtils;
import org.h3270.host.Field;
import org.h3270.host.InputField;
import org.h3270.host.S3270Screen;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.terminal.support.AbstractSnapshot;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;
import org.openlegacy.utils.BidiUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class H3270TerminalSnapshot extends AbstractSnapshot {

	private S3270Screen screen;
	private int sequence;
	private boolean rightToLeft;
	private boolean convertToLogical;

	public H3270TerminalSnapshot(S3270Screen screen, int sequence, boolean rightToLeft, boolean convertToLogical) {
		this.screen = screen;
		this.sequence = sequence;
		this.rightToLeft = rightToLeft;
		this.convertToLogical = convertToLogical;
	}

	public Object getDelegate() {
		return screen;
	}

	public Integer getSequence() {
		return sequence;
	}

	public String getCommand() {
		return null;
	}

	@Override
	protected List<TerminalPosition> initFieldSeperators() {
		List<Field> fields = screen.getFields();
		List<TerminalPosition> fieldSepeators = new ArrayList<TerminalPosition>();
		for (Field field : fields) {
			// don't count fields outside the snapshot
			if (field.getStartX() > 0) {
				int x = field.getStartX();
				if (isRightToLeft()) {
					x = getSize().getColumns() - x + 1;
				}
				fieldSepeators.add(new SimpleTerminalPosition(field.getStartY() + 1, x));
			}
		}
		return fieldSepeators;
	}

	@Override
	protected ScreenSize initScreenSize() {
		return new SimpleScreenSize(screen.getHeight(), screen.getWidth());
	}

	@Override
	protected TerminalPosition initCursorPosition() {
		InputField focusedField = screen.getFocusedField();
		if (focusedField == null) {
			return null;
		}
		int cursorColumn = focusedField.getStartX() + 1;
		if (rightToLeft) {
			cursorColumn = getSize().getColumns() - focusedField.getEndX();
		}
		return new SimpleTerminalPosition(focusedField.getStartY() + 1, cursorColumn);
	}

	@Override
	protected String initText() {
		List<TerminalField> fields = getFields();
		StringBuilder buffer = SnapshotUtils.initEmptyBuffer(getSize());
		TerminalField previousField = null;
		for (TerminalField terminalField : fields) {
			SnapshotUtils.placeContentOnBuffer(buffer, terminalField, getSize());
		}
		return buffer.toString();
	}

	@Override
	protected List<TerminalField> initFields() {
		List<Field> h3270Fields = screen.getFields();
		List<TerminalField> fields = new ArrayList<TerminalField>();
		for (Field field : h3270Fields) {
			// start column is 1 based, while field.getStartX() is 0 based
			int startColumn = field.getStartX() + 1; // 0 based -> convert to 1 based
			int endColumn = field.getEndX() + 1; // 0 based -> convert to 1 based
			if (field.isMultiline()) {
				// iterate through the multy-line field lines
				for (int i = field.getStartY(); i <= field.getEndY(); i++) {
					if (i > field.getStartY()) {
						startColumn = 1;
					}
					if (i < field.getEndY()) {
						endColumn = screen.getWidth();
					} else {
						// last row
						endColumn = field.getEndX() + 1; // 0 based -> convert to 1 based
					}
					addField(fields, field, startColumn, endColumn, i - field.getStartY());
				}
			} else {
				if (field.getEndX() >= 0) {
					addField(fields, field, startColumn, endColumn, 0);
				}
			}
		}
		Collections.sort(fields);
		return fields;
	}

	private void addField(List<TerminalField> fields, Field h3270field, int startColumn, int endColumn, int lineOffset) {
		H3270TerminalField field = null;
		if (rightToLeft) {
			int reversedStartColumn = getSize().getColumns() - endColumn + 1;
			int reversedEndColumn = getSize().getColumns() - startColumn + 1;
			field = new H3270TerminalField(h3270field, lineOffset, reversedStartColumn, reversedEndColumn, rightToLeft);
			String value = StringUtils.reverse(field.getValue());
			field.setValue(value, false);
			fields.add(field);
		} else {
			field = new H3270TerminalField(h3270field, lineOffset, startColumn, endColumn, rightToLeft);
			fields.add(field);
		}
		if (convertToLogical) {
			field.setVisualValue(field.getValue());
			String value = BidiUtil.convertToLogical(field.getValue(), field.isEditable());
			field.setValue(value, false);
		}
	}

	@Override
	protected void readExternal(TerminalPersistedSnapshot persistedSnapshot) {
		this.sequence = persistedSnapshot.getSequence();
	}

	@Override
	public boolean isRightToLeft() {
		return rightToLeft;
	}

}
