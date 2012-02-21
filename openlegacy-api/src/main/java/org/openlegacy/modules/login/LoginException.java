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
