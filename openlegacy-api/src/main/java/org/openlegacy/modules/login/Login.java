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

import org.openlegacy.EntityType;
import org.openlegacy.FieldType;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.SessionModule;

/**
 * A Login module is able to perform a login/log-off based on field and entity declaration
 * 
 * @author Roi Mor
 */
public interface Login extends SessionModule {

	public static final String ERROR_MESSAGE_LABEL = "Error Message";

	public static final String USER_FIELD_NAME = "user";
	public static final String PASSWORD_FIELD_NAME = "password";

	public static final String ERROR_FIELD_NAME = "errorMessage";

	void login(String user, String password) throws LoginException, RegistryException;

	/**
	 * 
	 * @param loginEntity
	 *            A login entity object
	 * @throws LoginException
	 * @throws RegistryException
	 */
	void login(Object loginEntity) throws LoginException, RegistryException;

	boolean isLoggedIn();

	String getLoggedInUser();

	void logoff();

	public static class LoginEntity implements EntityType {
	}

	public static class UserField implements FieldType {
	}

	public static class PasswordField implements FieldType {
	}

	public static class ErrorField implements FieldType {
	}
}
