package org.openlegacy.applinx;

import com.sabratec.util.GXPosition;

import org.openlegacy.terminal.ScreenPosition;

public class ApxPositionUtil {

	public static ScreenPosition toScreenPosition(GXPosition position) {
		return new ScreenPosition(position.getRow(), position.getColumn());
	}
}
