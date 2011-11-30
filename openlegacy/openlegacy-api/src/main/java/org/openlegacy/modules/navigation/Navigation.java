package org.openlegacy.modules.navigation;

import org.openlegacy.modules.SessionModule;
import org.openlegacy.terminal.ScreenEntityDescriptor;

import java.util.List;

/**
 * Defines navigation module. Adds extra methods to the session related to session navigation
 * 
 * 
 */
public interface Navigation extends SessionModule {

	List<ScreenEntityDescriptor> getPathFromRoot();
}
