package org.openlegacy.terminal.injectors;

import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;

public interface ScreenEntityDataInjector {

	void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen, boolean deep);
}
