package com.openlegacy.enterprise.ide.eclipse.views;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEntityModel;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.openlegacy.terminal.definitions.NavigationDefinition;

import java.util.ArrayList;
import java.util.List;

public class MapScreenEntityContentProvider implements IGraphEntityContentProvider {

	private Object[] screens;

	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object arg1, Object arg2) {
		viewer.refresh();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		screens = (Object[])inputElement;
		return screens;
	}

	@Override
	public Object[] getConnectedTo(Object entity) {
		ScreenEntityModel currentScreen = (ScreenEntityModel)entity;
		List<Object> connections = new ArrayList<Object>();

		for (Object obj : screens) {
			ScreenEntityModel screen = (ScreenEntityModel)obj;
			if (screen.compareUUID(currentScreen)) {
				continue;
			}
			NavigationDefinition navigationDefinition = screen.getDefinition().getNavigationDefinition();

			if (navigationDefinition == null) {
				continue;
			}
			if (navigationDefinition.getAccessedFromEntityName().equals(currentScreen.getName())) {
				connections.add(obj);
			}
		}
		return (connections.toArray());
	}

}
