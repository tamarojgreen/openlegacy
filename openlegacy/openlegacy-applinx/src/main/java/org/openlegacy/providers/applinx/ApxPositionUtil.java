package org.openlegacy.providers.applinx;

import com.sabratec.applinx.common.runtime.GXScreenPosition;
import com.sabratec.util.GXPosition;

import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.support.SimpleScreenPosition;

public class ApxPositionUtil {

	public static ScreenPosition toScreenPosition(GXPosition position) {
		return new SimpleScreenPosition(position.getRow(), position.getColumn());
	}

	public static ScreenPosition toScreenPosition(GXScreenPosition position) {
		return new SimpleScreenPosition(position.getRow(), position.getColumn());
	}

	public static GXPosition toPosition(ScreenPosition position) {
		return new GXPosition(position.getRow(), position.getColumn());
	}
}
