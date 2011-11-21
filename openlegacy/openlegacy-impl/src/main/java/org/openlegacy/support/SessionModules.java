package org.openlegacy.support;

import org.openlegacy.modules.SessionModule;

import java.util.List;

/**
 * Container class for
 * 
 */
public class SessionModules {

	private List<? extends SessionModule> modules;

	public List<? extends SessionModule> getModules() {
		return modules;
	}

	public void setModules(List<? extends SessionModule> modules) {
		this.modules = modules;
	}
}
