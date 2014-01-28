/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.services;

import org.openlegacy.terminal.TerminalSnapshot;

import java.util.List;

/**
 * A single screen identification which determine if a given terminal screen matches the given {@link TerminalSnapshot}
 * 
 * @author Roi Mor
 */
public interface ScreenIdentification {

	List<ScreenIdentifier> getScreenIdentifiers();

	boolean match(TerminalSnapshot terminalSnapshot);

	void addIdentifier(ScreenIdentifier screenIdentifier);
}
