package org.openlegacy.authorization;

import org.openlegacy.modules.login.User;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;

import java.util.List;

public interface AuthorizationService {

	boolean isAuthorized(User user, Class<?> entityClass);

	MenuItem filterMenu(User user, MenuItem rootMenu);

	List<MenuItem> filterMenus(User user, List<MenuItem> flatMenus);

	boolean canAssignField(User user, FieldAssignDefinition fieldAssignDefinition);
}