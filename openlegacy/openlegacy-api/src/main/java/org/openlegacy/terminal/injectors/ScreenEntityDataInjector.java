package org.openlegacy.terminal.injectors;

import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;

/**
 * An injector is responsible for populating a screen entity based on the given content provider
 * 
 */
public interface ScreenEntityDataInjector {

	void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen);
}
