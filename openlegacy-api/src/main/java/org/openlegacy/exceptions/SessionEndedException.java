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
package org.openlegacy.exceptions;

/**
 * This exception is thrown when/after an action is performed and the session ended
 * 
 */
public class SessionEndedException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public SessionEndedException(String s) {
		super(s);
	}

	public SessionEndedException(Exception e) {
		super(e);
	}
}
