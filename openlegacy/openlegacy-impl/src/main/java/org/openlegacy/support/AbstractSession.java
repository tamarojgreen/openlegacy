package org.openlegacy.support;

import org.openlegacy.Session;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.modules.SessionModule;

import java.text.MessageFormat;
import java.util.List;

/**
 * An abstract session class which exposes modules management functionality
 * 
 * 
 */
public abstract class AbstractSession implements Session {

	private SessionModules sessionModules;

	@SuppressWarnings("unchecked")
	public <M extends SessionModule> M getModule(Class<M> module) {
		if (sessionModules == null) {
			throw (new OpenLegacyException("No modules defined for session"));
		}
		List<? extends SessionModule> modules = sessionModules.getModules();
		for (SessionModule registeredModule : modules) {
			if (module.isAssignableFrom(registeredModule.getClass())) {
				return (M)registeredModule;
			}
		}
		throw (new OpenLegacyException(MessageFormat.format("No module {0} defined for session", module)));
	}

	public void setSessionModules(SessionModules sessionModules) {
		this.sessionModules = sessionModules;
	}

	public SessionModules getSessionModules() {
		return sessionModules;
	}
}
