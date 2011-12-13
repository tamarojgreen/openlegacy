package org.openlegacy.modules.support.menu;

import org.openlegacy.modules.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class SimpleMenuItem implements MenuItem {

	private Object action;
	private String caption;
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	public SimpleMenuItem(Object action, String caption) {
		this.action = action;
		this.caption = caption;
	}

	public Object getAction() {
		return action;
	}

	public String getCaption() {
		return caption;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

}
