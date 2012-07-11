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
package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;

/**
 * Throw when a terminal action is not mapped to a <code>TerminalConnection</code> within <code>TerminalSendAction</code>
 * 
 */
public class TerminalActionNotMappedException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public TerminalActionNotMappedException(Exception e) {
		super(e);
	}

	public TerminalActionNotMappedException(String s) {
		super(s);
	}

	public TerminalActionNotMappedException(String s, Exception e) {
		super(s, e);
	}
}
