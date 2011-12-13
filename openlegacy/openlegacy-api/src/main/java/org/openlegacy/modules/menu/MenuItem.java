package org.openlegacy.modules.menu;

import java.util.List;

public interface MenuItem {

	Object getAction();

	String getCaption();

	List<MenuItem> getMenuItems();
}
