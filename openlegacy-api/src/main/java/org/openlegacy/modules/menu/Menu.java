package org.openlegacy.modules.menu;

import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.modules.SessionModule;

public interface Menu extends SessionModule {

	public static final String SELECTION_LABEL = "Menu Selection";

	MenuItem getMenuTree();

	MenuItem getMenuTree(Class<?> menuEntityClass);

	public static class MenuEntity implements EntityType {
	}

	public static class MenuSelectionField implements FieldType {
	}
}
