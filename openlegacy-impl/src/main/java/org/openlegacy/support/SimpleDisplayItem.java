package org.openlegacy.support;

import org.openlegacy.DisplayItem;

public class SimpleDisplayItem implements DisplayItem {

	private Object value;
	private Object display;

	public SimpleDisplayItem(Object value, Object display) {
		this.value = value;
		this.display = display;
	}

	public Object getValue() {
		return value;
	}

	public Object getDisplay() {
		return display;
	}

}
