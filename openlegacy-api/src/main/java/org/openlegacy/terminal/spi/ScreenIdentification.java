package org.openlegacy.terminal.spi;

import org.openlegacy.terminal.TerminalSnapshot;

import java.util.List;

/**
 * A single screen identification which determine if a given terminal screen is found
 * 
 */
public interface ScreenIdentification {

	List<ScreenIdentifier> getScreenIdentifiers();

	boolean match(TerminalSnapshot terminalSnapshot);

	void addIdentifier(ScreenIdentifier screenIdentifier);
}
