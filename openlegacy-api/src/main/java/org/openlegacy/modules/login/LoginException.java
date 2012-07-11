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
package org.openlegacy.modules.login;

import org.openlegacy.exceptions.OpenLegacyException;

/**
 * A login exception is thrown when a session is not able to pass the login phase
 * 
 */
public class LoginException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public LoginException(String s) {
		super(s);
	}
}
