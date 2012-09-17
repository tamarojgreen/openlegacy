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

import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;

/**
 * Session navigator enables navigating from the current screen entity to the specified target screen entity class
 * 
 * @author Roi Mor
 * 
 */
public interface SessionNavigator {

	void navigate(TerminalSession terminalSession, Class<?> targetScreenEntityClass, Object... keys)
			throws ScreenEntityNotAccessibleException;
}
