package org.openlegacy.applinx;

import com.sabratec.applinx.common.runtime.GXScreenPosition;
import com.sabratec.applinx.common.runtime.field.GXIField;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;

import org.openlegacy.adapter.terminal.ScreenDisplayUtils;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;

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
		return new ScreenSize(screen.getSize().getHeight(), screen.getSize().getWidth());
	}
}
