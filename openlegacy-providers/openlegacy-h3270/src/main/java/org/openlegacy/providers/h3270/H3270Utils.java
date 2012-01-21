package org.openlegacy.providers.h3270;

import org.h3270.host.Field;
import org.openlegacy.terminal.Color;

public class H3270Utils {

	public static Color convertForeColor(int h3270Color) {
		Color color = convertColor(h3270Color);
		return (color != null ? color : Color.GREEN);
	}

	public static Color convertBackColor(int h3270Color) {
		Color color = convertColor(h3270Color);
		return (color != null ? color : Color.BLACK);
	}

	private static Color convertColor(int h3270Color) {
		switch (h3270Color) {
			case Field.ATTR_COL_BLUE:
				return Color.BLUE;

			case Field.ATTR_COL_GREEN:
				return Color.GREEN;
			case Field.ATTR_COL_PINK:
				return Color.PINK;
			case Field.ATTR_COL_RED:
				return Color.RED;
			case Field.ATTR_COL_TURQUOISE:
				return Color.LIGHT_GREEN;
			case Field.ATTR_COL_WHITE:
				return Color.WHITE;
			case Field.ATTR_COL_YELLOW:
				return Color.YELLOW;
		}
		return null;
	}
}
