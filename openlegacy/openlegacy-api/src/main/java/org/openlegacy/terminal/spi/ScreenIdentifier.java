package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.TerminalSnapshot;

/**
 * An interface for a terminal screen identifier. Determine whether the given identifier matches the given terminal screen
 * 
 */
public interface ScreenIdentifier extends TerminalPositionContainer {

	boolean match(TerminalSnapshot terminalSnapshot);
}
