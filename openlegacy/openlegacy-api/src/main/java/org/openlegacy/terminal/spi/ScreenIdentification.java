package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalScreen;

/**
 * A single screen identification which determine if a given terminal screen is found  
 *
 */
public interface ScreenIdentification {

	String getName();

	String match(TerminalScreen hostScreen);
}
