package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalScreen;

import java.util.List;

/**
 * A single screen identification which determine if a given terminal screen is found
 * 
 */
public interface ScreenIdentification {

	List<ScreenIdentifier> getScreenIdentifiers();

	boolean match(TerminalScreen hostScreen);

	void addIdentifier(ScreenIdentifier screenIdentifier);
}
