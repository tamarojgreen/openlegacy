package org.openlegacy.terminal.injectors;

import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.providers.DefinitionsProvider;

/**
 * An injector is responsible for populating a screen entity based on the given content provider
 * 
 */
public interface ScreenEntityDataInjector<P extends DefinitionsProvider> {

	P getDefinitionsProvider();

	void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen, boolean deep);
}
