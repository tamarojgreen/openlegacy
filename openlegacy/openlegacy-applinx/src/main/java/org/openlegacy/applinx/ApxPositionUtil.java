package org.openlegacy.applinx;

import com.sabratec.util.GXPosition;

import org.openlegacy.adapter.terminal.SimpleScreenPosition;
import org.openlegacy.terminal.ScreenPosition;

public class ApxPositionUtil {

	public static ScreenPosition toScreenPosition(GXPosition position) {
		return new SimpleScreenPosition(position.getRow(), position.getColumn());
	}

	public static GXPosition toPosition(ScreenPosition position) {
		return new GXPosition(position.getRow(), position.getColumn());
	}
}
