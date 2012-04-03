package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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

	@Override
	public String toString() {
		return screenIdentifiers.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ScreenIdentification)) {
			return false;
		}
		ScreenIdentification other = (ScreenIdentification)obj;
		return new EqualsBuilder().append(screenIdentifiers.toArray(), other.getScreenIdentifiers().toArray()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(screenIdentifiers.toArray()).toHashCode();
	}
}
