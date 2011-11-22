package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation for a screen identification within a ScreenIdentifier
 * 
 */
public class SimpleScreenIdentification implements ScreenIdentification {

	private List<ScreenIdentifier> screenIdentifiers = new ArrayList<ScreenIdentifier>();

	public List<ScreenIdentifier> getScreenIdentifiers() {
		return screenIdentifiers;
	}

	public void addIdentifier(ScreenIdentifier screenIdentifier) {
		screenIdentifiers.add(screenIdentifier);
	}

	public boolean match(TerminalSnapshot terminalSnapshot) {
		List<ScreenIdentifier> identifiers = screenIdentifiers;
		for (ScreenIdentifier screenIdentifier : identifiers) {
			if (!screenIdentifier.match(terminalSnapshot)) {
				return false;
			}
		}
		return true;
	}

}
