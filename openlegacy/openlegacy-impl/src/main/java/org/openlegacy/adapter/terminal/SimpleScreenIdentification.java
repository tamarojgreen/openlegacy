package org.openlegacy.adapter.terminal;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.ScreenIdentification;
import org.openlegacy.terminal.spi.ScreenIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation for a screen identification within a ScreenIdentifier
 * 
 */
public class SimpleScreenIdentification implements ScreenIdentification {

	private String screenName;
	private List<ScreenIdentifier> screenIdentifiers = new ArrayList<ScreenIdentifier>();

	public SimpleScreenIdentification(String screenName) {
		this.screenName = screenName;
	}

	public void addIdentifier(ScreenIdentifier screenIdentifier) {
		screenIdentifiers.add(screenIdentifier);
	}

	public String match(TerminalScreen hostScreen) {
		List<ScreenIdentifier> identifiers = screenIdentifiers;
		for (ScreenIdentifier screenIdentifier : identifiers) {
			if (!screenIdentifier.match(hostScreen)) {
				return null;
			}
		}
		return screenName;
	}

	public String getName() {
		return screenName;
	}
}
