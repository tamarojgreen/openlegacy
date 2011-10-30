package org.openlegacy.support;

import org.openlegacy.HostSessionModule;

import java.util.List;

/**
 * Container class for
 * 
 * @author RoiM
 * 
 */
public class HostSessionModules {

	private List<? extends HostSessionModule> modules;

	public List<? extends HostSessionModule> getModules() {
		return modules;
	}

	public void setModules(List<? extends HostSessionModule> modules) {
		this.modules = modules;
	}
}
