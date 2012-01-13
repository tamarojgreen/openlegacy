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
	private List<TerminalPosition> fieldSeperators;
	private String text;

	private ScreenSize screenSize;

	private TerminalPosition cursor;

	public ApxTerminalSnapshot(GXRuntimeScreen screen) {
		this.screen = screen;
	}

	public String getText() {
		if (text != null) {
			return text;
		}
		text = new String(screen.getBuffers().getTextBuffer());
		return text;
	}

	public Object getDelegate() {
		return new GXClientScreen(screen);
	}

	public ScreenSize getSize() {
		if (screenSize == null) {
			screenSize = new SimpleScreenSize(screen.getSize().getHeight(), screen.getSize().getWidth());
		}
		return screenSize;
	}

	public List<TerminalPosition> getFieldSeperators() {
		if (fieldSeperators != null) {
			return fieldSeperators;
		}
		@SuppressWarnings("unchecked")
		List<GXScreenPosition> attributes = screen.getAttributePositions();

		fieldSeperators = new ArrayList<TerminalPosition>();
		for (Object object : attributes) {
			fieldSeperators.add(ApxPositionUtil.toScreenPosition((GXScreenPosition)object));
		}
		return fieldSeperators;

	}

	public TerminalPosition getCursorPosition() {
		if (cursor == null) {
			cursor = ApxPositionUtil.toScreenPosition(screen.getCursorPosition());
		}
		return cursor;
	}

	public Integer getSequence() {
		return screen.getSequentialNumber();
	}

	public String getCommand() {
		return null;
	}

	@Override
	protected List<TerminalField> buildAllFields() {
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
