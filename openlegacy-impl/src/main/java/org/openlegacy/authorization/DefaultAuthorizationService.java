package org.openlegacy.authorization;

import org.apache.commons.collections.CollectionUtils;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.User;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class DefaultAuthorizationService implements AuthorizationService {

	@Inject
	private EntitiesRegistry<?, ?, ?> entitiesRegistry;

	private boolean allowNullUser = true;

	public MenuItem filterMenu(User user, MenuItem rootMenu) {
		if (rootMenu == null) {
			return null;
		}
		checkNullUser(user);
		MenuItem userMenu = rootMenu.clone();
		if (user != null) {
			String userRole = (String)user.getProperties().get(Login.USER_ROLE_PROPERTY);
			if (userRole != null) {
				List<MenuItem> userItems = new ArrayList<MenuItem>();
				userItems.add(userMenu);
				String[] userRoles = userRole.split(",");
				filterMenuItems(userRoles, userItems);
			}
		}
		return userMenu;
	}

	private void checkNullUser(User user) {
		if (!allowNullUser) {
			Assert.notNull(user, "Null user is not allowed");
		}
	}

	public List<MenuItem> filterMenus(User user, List<MenuItem> flatMenus) {
		checkNullUser(user);
		List<MenuItem> userFlatMenus1 = clone(flatMenus);
		if (user != null) {
			String userRole = (String)user.getProperties().get(Login.USER_ROLE_PROPERTY);
			if (userRole != null) {
				// have working copy for the user
				String[] userRoles = userRole.split(",");
				filterMenuItems(userRoles, userFlatMenus1);
			}
		}
		return userFlatMenus1;
	}

	private void filterMenuItems(String[] userRoles, List<MenuItem> menuItems) {
		MenuItem[] items = menuItems.toArray(new MenuItem[menuItems.size()]);
		for (MenuItem menuItem : items) {
			String targetEntityName = menuItem.getTargetEntityName();
			EntityDefinition<?> entityDefinition = entitiesRegistry.get(targetEntityName);
			if (entityDefinition.getRoles() != null && entityDefinition.getRoles().size() > 0) {
				if (!CollectionUtils.containsAny(entityDefinition.getRoles(), Arrays.asList(userRoles))) {
					menuItems.remove(menuItem);
				}
			}
			filterMenuItems(userRoles, menuItem.getMenuItems());
		}
	}

	private static List<MenuItem> clone(List<MenuItem> flatMenuEntries) {
		List<MenuItem> userFlatMenus = new ArrayList<MenuItem>();
		for (MenuItem menuItem : flatMenuEntries) {
			userFlatMenus.add(menuItem.clone());
		}
		return userFlatMenus;
	}

	public boolean isAuthorized(User user, Class<?> entityClass) {
		checkNullUser(user);

		if (user == null) {
			return true;
		}
		String userRole = (String)user.getProperties().get(Login.USER_ROLE_PROPERTY);
		if (userRole == null) {
			return true;
		}
		EntityDefinition<?> entityDefinition = entitiesRegistry.get(entityClass);
		if (entityDefinition.getRoles() != null && entityDefinition.getRoles().size() > 0) {
			return entityDefinition.getRoles().contains(userRole);
		}
		return true;
	}

	public boolean canAssignField(User user, FieldAssignDefinition fieldAssignDefinition) {
		checkNullUser(user);
		boolean doAssign = true;
		if (fieldAssignDefinition.getRole() != null) {
			if (user != null) {
				Object userRole = user.getProperties().get(Login.USER_ROLE_PROPERTY);
				if (userRole != null) {
					if (!userRole.equals(fieldAssignDefinition.getRole())) {
						doAssign = false;
					}
				}
			}
		}
		return doAssign;

	}

	public void setAllowNullUser(boolean allowNullUser) {
		this.allowNullUser = allowNullUser;
	}
}
