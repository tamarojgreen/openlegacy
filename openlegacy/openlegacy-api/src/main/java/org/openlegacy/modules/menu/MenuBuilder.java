package org.openlegacy.modules.menu;

public interface MenuBuilder {

	/**
	 * Retrieve a menu tree from the given menu class
	 * 
	 * @param menuEntityClass
	 * @return
	 */
	MenuItem getMenuTree(Class<?> menuEntityClass);

	/**
	 * Find the root menu and retrieve a full menu tree
	 * 
	 * @return
	 */
	MenuItem getMenuTree();
}
