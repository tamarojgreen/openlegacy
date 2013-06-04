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
package org.openlegacy.rpc;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalSendAction;

/**
 * Thrown when a terminal action is not mapped in a {@link TerminalActionMapper}
 * 
 * @see TerminalSendAction
 * 
 * @author Roi Mor
 * 
 */
public class RpcActionNotMappedException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public RpcActionNotMappedException(Exception e) {
		super(e);
	}

	public RpcActionNotMappedException(String s) {
		super(s);
	}

	public RpcActionNotMappedException(String s, Exception e) {
		super(s, e);
	}
}
