package org.openlegacy.applinx;

import com.sabratec.util.GXPosition;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.SimpleScreenPosition;

public class ApxPositionUtil {

	public static ScreenPosition toScreenPosition(GXPosition position) {
		return new SimpleScreenPosition(position.getRow(), position.getColumn());
	}
}
