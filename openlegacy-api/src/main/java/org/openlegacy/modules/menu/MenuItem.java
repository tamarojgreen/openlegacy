package org.openlegacy.modules.menu;

import java.util.List;

public interface MenuItem {

	Class<?> getTargetEntity();

	String getDisplayName();
	
	List<MenuItem> getMenuItems();
}
