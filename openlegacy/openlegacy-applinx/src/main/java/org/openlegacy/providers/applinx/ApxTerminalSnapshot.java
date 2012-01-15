package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.internal.GXClientScreen;
import com.sabratec.applinx.common.runtime.GXScreenPosition;
import com.sabratec.applinx.common.runtime.field.GXIField;
import com.sabratec.applinx.common.runtime.screen.GXRuntimeScreen;

import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.AbstractSnapshot;
import org.openlegacy.terminal.support.SimpleScreenSize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApxTerminalSnapshot extends AbstractSnapshot {

	private static final long serialVersionUID = 1L;

	private final GXRuntimeScreen screen;

	public ApxTerminalSnapshot(GXRuntimeScreen screen) {
		this.screen = screen;
	}

	@Override
	protected String initText() {
		return new String(screen.getBuffers().getTextBuffer());
	}

	public Object getDelegate() {
		return new GXClientScreen(screen);
	}

	@Override
	protected ScreenSize initScreenSize() {
		return new SimpleScreenSize(screen.getSize().getHeight(), screen.getSize().getWidth());
	}

	@Override
	protected List<TerminalPosition> initFieldSeperators() {
		@SuppressWarnings("unchecked")
		List<GXScreenPosition> attributes = screen.getAttributePositions();

		List<TerminalPosition> fieldSeperators = new ArrayList<TerminalPosition>();
		for (Object object : attributes) {
			fieldSeperators.add(ApxPositionUtil.toScreenPosition((GXScreenPosition)object));
		}
		return fieldSeperators;

	}

	@Override
	protected TerminalPosition initCursorPosition() {
		return ApxPositionUtil.toScreenPosition(screen.getCursorPosition());
	}

	public Integer getSequence() {
		return screen.getSequentialNumber();
	}

	public String getCommand() {
		return null;
	}

	@Override
	protected List<TerminalField> initFields() {
		List<TerminalField> fields = new ArrayList<TerminalField>();

		@SuppressWarnings("unchecked")
		Collection<GXIField> apxInputFields = screen.getFields();

		fields = new ArrayList<TerminalField>();
		for (GXIField apxField : apxInputFields) {
			fields.add(new ApxTerminalField(apxField));
		}
		return fields;
	}

}
