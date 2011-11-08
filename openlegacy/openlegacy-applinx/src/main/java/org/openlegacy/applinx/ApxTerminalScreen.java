package org.openlegacy.applinx;

import com.sabratec.applinx.common.runtime.GXScreenPosition;
import com.sabratec.applinx.common.runtime.field.GXIField;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.terminal.utils.ScreenDisplayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApxTerminalScreen implements TerminalScreen {

	private final GXRuntimeScreen screen;

	public ApxTerminalScreen(GXRuntimeScreen screen) {
		this.screen = screen;
	}

	@Override
	public String toString() {
		return ScreenDisplayUtils.toString(this, true);
	}

	public String getText() {
		return new String(screen.getBuffers().getTextBuffer());
	}

	public String getText(ScreenPosition position, int length) {
		int beginIndex = ((position.getRow() - 1) * getSize().getColumns()) + (position.getColumn() - 1);
		return getText().substring(beginIndex, beginIndex + length);
	}

	public TerminalField getField(ScreenPosition position) {
		GXIField field = screen.getFields().get(new GXScreenPosition(position.getRow(), position.getColumn(), screen.getSize()));
		return new ApxTerminalField(field);
	}

	public Object getDelegate() {
		return screen;
	}

	public Collection<TerminalField> getEditableFields() {
		@SuppressWarnings("unchecked")
		Collection<GXIField> apxInputFields = screen.getFields().getUnprotectedFields().values();

		List<TerminalField> terminalFields = new ArrayList<TerminalField>();
		for (GXIField apxField : apxInputFields) {
			terminalFields.add(new ApxTerminalField(apxField));
		}
		return terminalFields;
	}

	public ScreenSize getSize() {
		return new SimpleScreenSize(screen.getSize().getHeight(), screen.getSize().getWidth());
	}

	public List<TerminalRow> getRows() {
		@SuppressWarnings("unchecked")
		Collection<GXIField> apxFields = screen.getFields();
		List<TerminalRow> rows = new ArrayList<TerminalRow>();

		ApxTerminalRow currentRow = new ApxTerminalRow(1);
		for (GXIField apxField : apxFields) {
			if (apxField.getPosition().getRow() > currentRow.getRowNumber()) {
				rows.add(currentRow);
				currentRow = new ApxTerminalRow(apxField.getPosition().getRow());
			}
			currentRow.getFields().add(new ApxTerminalField(apxField));
		}
		rows.add(currentRow);
		return rows;
	}

	public SnapshotType getSnapshotType() {
		return SnapshotType.INCOMING;
	}

	public List<ScreenPosition> getAttributes() {
		@SuppressWarnings("unchecked")
		List<GXScreenPosition> attributes = screen.getAttributePositions();

		List<ScreenPosition> screenPositions = new ArrayList<ScreenPosition>();
		for (Object object : attributes) {
			screenPositions.add(ApxPositionUtil.toScreenPosition((GXScreenPosition)object));
		}
		return screenPositions;

	}
}
