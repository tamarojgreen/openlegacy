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
package org.openlegacy.terminal.actions;

import org.openlegacy.SessionAction;
import org.openlegacy.terminal.TerminalSession;

/**
 * A terminal action is an action performed on a <code>TerminalSession</code> Can be either a mapped action (
 * <code>TerminalActions.ENTER(), etc</code>) or a custom action which implements this interface and perform a sequence of mapped
 * actions (macro style)
 * 
 * @author Roi Mor
 */
public interface TerminalAction extends SessionAction<TerminalSession> {

	public static final String NONE = "none";

	public enum AdditionalKey {
		NONE,
		SHIFT,
		CTRL,
		ALT
	}
}
