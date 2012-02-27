package org.openlegacy.modules.navigation;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.modules.SessionModule;

import java.util.List;

/**
 * Defines navigation module. Adds extra methods to the session related to session navigation
 * 
 * 
 */
public interface Navigation extends SessionModule {

	List<EntityDescriptor> getPaths();
}
