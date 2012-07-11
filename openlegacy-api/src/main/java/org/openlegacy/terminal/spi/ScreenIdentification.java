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
