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
package org.openlegacy.terminal.modules.login;

import org.openlegacy.SessionModuleMetadata;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import javax.inject.Inject;

@Component
public class LoginMetadata implements SessionModuleMetadata, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	private ScreenFieldDefinition userField;
	private ScreenFieldDefinition passwordField;
	private ScreenFieldDefinition errorField;
	private ScreenEntityDefinition loginScreenDefinition;

	private Class<?> loginClass;

	public void initCache() {

		if (loginScreenDefinition != null) {
			return;
		}
		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);

		if (loginClass == null) {
			loginScreenDefinition = screenEntitiesRegistry.getSingleEntityDefinition(Login.LoginEntity.class);
		} else {
			loginScreenDefinition = screenEntitiesRegistry.get(loginClass);
		}

		if (loginScreenDefinition == null) {
			return;
		}

		userField = loginScreenDefinition.getFirstFieldDefinition(Login.UserField.class);
		passwordField = loginScreenDefinition.getFirstFieldDefinition(Login.PasswordField.class);
		errorField = loginScreenDefinition.getFirstFieldDefinition(Login.ErrorField.class);

		Class<?> entityClass = loginScreenDefinition.getEntityClass();
		if (userField == null) {
			throw (new RegistryException("User field not defined in " + entityClass));
		}
		if (passwordField == null) {
			throw (new RegistryException("Password field not defined in " + entityClass));
		}
		if (errorField == null) {
			throw (new RegistryException("Error field not defined in " + entityClass));
		}
	}

	public ScreenFieldDefinition getUserField() {
		return userField;
	}

	public ScreenFieldDefinition getPasswordField() {
		return passwordField;
	}

	public ScreenFieldDefinition getErrorField() {
		return errorField;
	}

	public ScreenEntityDefinition getLoginScreenDefinition() {
		return loginScreenDefinition;
	}

	public void setLoginClass(Class<?> clazz) {
		loginClass = clazz;
	}
}
