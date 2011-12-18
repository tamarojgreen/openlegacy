package org.openlegacy.designtime.terminal.analyzer.modules.navigation;

import org.openlegacy.terminal.definitions.SimpleScreenNavigationDefinition;

public class ScreenNavigationDesignTimeDefinition extends SimpleScreenNavigationDefinition {

	private String accessedFromEntityName;

	public void setAccessedFromEntityName(String accessedFromEntityName) {
		this.accessedFromEntityName = accessedFromEntityName;
	}

	public String getAccessedFromEntityName() {
		return accessedFromEntityName;
	}
}
