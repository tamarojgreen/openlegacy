package org.openlegacy.modules.login;

import org.openlegacy.exceptions.OpenLegacyException;

public class LoginException extends OpenLegacyException {

	private static final long serialVersionUID = 1L;

	public LoginException(String s) {
		super(s);
	}
}
