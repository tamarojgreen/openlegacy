package org.openlegacy.modules.menu;

import java.util.List;

public interface MenuItem {

	Class<?> getTargetEntity();

	String getCaption();

	List<MenuItem> getMenuItems();
}
