package org.openlegacy.terminal.modules.navigation;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.SimpleEntityDescriptor;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.utils.ReflectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultTerminalNavigationModule extends TerminalSessionModuleAdapter implements Navigation {

	private static final long serialVersionUID = 1L;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	private TerminalAction defaultExitAction = TerminalActions.F3();

	public List<EntityDescriptor> getPaths() {

		Object currentEntity = getSession().getEntity();

		List<EntityDescriptor> pathEntries = new ArrayList<EntityDescriptor>();
		if (currentEntity == null) {
			return pathEntries;
		}

		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());

		boolean first = true;
		while (currentEntityDefinition != null) {
			SimpleEntityDescriptor pathEntry = new SimpleEntityDescriptor(currentEntityDefinition.getEntityClass(),
					currentEntityDefinition.getEntityName(), currentEntityDefinition.getDisplayName());
			pathEntry.setCurrent(first);
			first = false;

			pathEntries.add(pathEntry);

			NavigationDefinition navigationDefinition = currentEntityDefinition.getNavigationDefinition();
			currentEntityDefinition = navigationDefinition != null ? screenEntitiesRegistry.get(navigationDefinition.getAccessedFrom())
					: null;

		}

		Collections.reverse(pathEntries);

		return pathEntries;
	}

	public void setDefaultExitActionClass(Class<? extends TerminalAction> defaultExitAction) {
		this.defaultExitAction = ReflectionUtil.newInstance(defaultExitAction);
	}

	public TerminalAction getDefaultExitAction() {
		return defaultExitAction;
	}
}
