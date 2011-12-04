package org.openlegacy.terminal.modules.navigation;

import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.ScreenEntityDescriptor;
import org.openlegacy.terminal.SimpleScreenEntityDescriptor;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class DefaultNavigationModule extends TerminalSessionModuleAdapter implements Navigation {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public List<ScreenEntityDescriptor> getPathFromRoot() {

		Object currentEntity = getSession().getEntity();

		ScreenEntityDefinition currentEntityDefinition = screenEntitiesRegistry.get(currentEntity.getClass());

		List<ScreenEntityDescriptor> pathEntries = new ArrayList<ScreenEntityDescriptor>();

		boolean first = true;
		while (currentEntityDefinition != null) {
			ScreenEntityDescriptor pathEntry = new SimpleScreenEntityDescriptor(currentEntityDefinition.getEntityClass(),
					currentEntityDefinition.getEntityName(), currentEntityDefinition.getDisplayName(), first);
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
