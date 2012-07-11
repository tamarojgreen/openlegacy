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

import org.openlegacy.exceptions.EntityNotFoundException;

/**
 * An exception for request a field from a <code>TerminalSnapshot</code> by position which doesn't exists on the snapshot
 * 
 */
public class PositionNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public PositionNotFoundException(Exception e) {
		super(e);
	}

	public PositionNotFoundException(String s) {
		super(s);
	}

}
