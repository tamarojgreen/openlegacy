package org.openlegacy.support;

import org.openlegacy.HostSession;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.modules.HostSessionModule;

import java.text.MessageFormat;
import java.util.List;

/**
 * An abstract session class which exposes modules management functionality
 * 
 * 
 */
public abstract class AbstractHostSession implements HostSession {

	private HostSessionModules sessionModules;

	@SuppressWarnings("unchecked")
	public <M extends HostSessionModule> M getModule(Class<M> module) {
		if (sessionModules == null) {
			throw (new OpenLegacyException("No modules defined for session"));
		}
		List<? extends HostSessionModule> modules = sessionModules.getModules();
		for (HostSessionModule registeredModule : modules) {
			if (module.isAssignableFrom(registeredModule.getClass())) {
				return (M)registeredModule;
			}
		}
		throw (new OpenLegacyException(MessageFormat.format("No module {0} defined for session", module)));
	}

	public void setSessionModules(HostSessionModules sessionModules) {
		this.sessionModules = sessionModules;
	}

	public HostSessionModules getSessionModules() {
		return sessionModules;
	}
}
