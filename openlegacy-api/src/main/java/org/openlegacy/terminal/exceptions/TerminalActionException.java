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

import org.openlegacy.exceptions.EntityNotAccessibleException;

/**
 * An exception indicating a problem with a terminal action execution
 * 
 * @author Roi Mor
 * 
 */
public class TerminalActionException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public TerminalActionException(Exception e) {
		super(e);
	}

	public TerminalActionException(String s) {
		super(s);
	}

}
