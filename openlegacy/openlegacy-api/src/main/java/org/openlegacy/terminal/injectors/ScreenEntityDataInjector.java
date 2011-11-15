package org.openlegacy.terminal.injectors;

import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;

/**
 * An injector is responsible for populating a screen entity based on the given content provider
 * 
 */
public interface ScreenEntityDataInjector {

	void inject(ScreenPojoFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen);
}
