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
 * This exception is typically thrown when a session is unable to access the requested screen entity
 * 
 */
public class ScreenEntityNotAccessibleException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public ScreenEntityNotAccessibleException(Exception e) {
		super(e);
	}

	public ScreenEntityNotAccessibleException(String s) {
		super(s);
	}

}
