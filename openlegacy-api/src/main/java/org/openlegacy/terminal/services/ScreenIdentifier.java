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
package org.openlegacy.terminal.services;

import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.TerminalSnapshot;

/**
 * An interface for a terminal screen identifier. Determine whether the given identifier matches the given terminal screen
 * 
 * @author Roi Mor
 */
public interface ScreenIdentifier extends TerminalPositionContainer {

	boolean match(TerminalSnapshot terminalSnapshot);
}
