package org.openlegacy.terminal;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length 
 *
 */
public class FieldMapping {

	private final String name;
	private final ScreenPosition screenPosition;
	private final int length;

	public FieldMapping(String name, ScreenPosition screenPosition,
			int length) {
		this.name = name;
		this.screenPosition = screenPosition;
		this.length = length;
	}

	public ScreenPosition getScreenPosition() {
		return screenPosition;
	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return name;
	}
}
