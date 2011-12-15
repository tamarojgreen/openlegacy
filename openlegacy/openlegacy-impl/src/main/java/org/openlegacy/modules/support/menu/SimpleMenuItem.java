package org.openlegacy.modules.support.menu;

import org.openlegacy.modules.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class SimpleMenuItem implements MenuItem {

	private Class<?> targetEntity;
	private String caption;
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	public SimpleMenuItem(Class<?> targetEntity, String caption) {
		this.targetEntity = targetEntity;
		this.caption = caption;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public String getCaption() {
		return caption;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

}
