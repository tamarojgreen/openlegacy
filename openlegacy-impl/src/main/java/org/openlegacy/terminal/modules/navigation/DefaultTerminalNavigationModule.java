package org.openlegacy.terminal.modules.navigation;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.SimpleEntityDescriptor;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultTerminalNavigationModule extends TerminalSessionModuleAdapter implements Navigation {

	private static final long serialVersionUID = 1L;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public List<EntityDescriptor> getPathFromRoot() {

		Object currentEntity = getSession().getEntity();

		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());

		List<EntityDescriptor> pathEntries = new ArrayList<EntityDescriptor>();

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

}
