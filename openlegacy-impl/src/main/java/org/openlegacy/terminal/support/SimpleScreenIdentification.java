/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.services.ScreenIdentification;
import org.openlegacy.terminal.services.ScreenIdentifier;

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
