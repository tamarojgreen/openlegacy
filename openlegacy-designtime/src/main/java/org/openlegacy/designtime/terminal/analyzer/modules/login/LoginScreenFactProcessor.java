/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.terminal.analyzer.modules.login;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

import javax.inject.Inject;

public class LoginScreenFactProcessor implements ScreenFactProcessor {

	@Inject
	private ScreenEntityDefinitionsBuilderUtils screenEntityDefinitionsBuilderUtils;

	private String userFieldName = Login.USER_FIELD_NAME;
	private String passwordFieldName = Login.PASSWORD_FIELD_NAME;
	private String errorFieldName = Login.ERROR_FIELD_NAME;

	@Override
	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {

		LoginScreenFact loginScreenFact = (LoginScreenFact)screenFact;

		screenEntityDefinition.setType(Login.LoginEntity.class);
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(Login.LoginEntity.class));

		SimpleScreenFieldDefinition userFieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinitionsBuilderUtils.addField(
				screenEntityDefinition, loginScreenFact.getUserField(), loginScreenFact.getUserLabelField());
		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, userFieldDefinition, Login.UserField.class);
		// set user field name to fixed name
		userFieldDefinition.setName(userFieldName);

		TerminalField passwordLabelField = loginScreenFact.getPasswordLabelField();
		SimpleScreenFieldDefinition passwordFieldDefinition = null;
		if (passwordLabelField != null) {
			passwordFieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinitionsBuilderUtils.addField(
					screenEntityDefinition, loginScreenFact.getPasswordField(), passwordLabelField);
			// set password field name to fixed name
			passwordFieldDefinition.setName(passwordFieldName);
		} else {
			passwordFieldDefinition = (SimpleScreenFieldDefinition)screenEntityDefinitionsBuilderUtils.addField(
					screenEntityDefinition, loginScreenFact.getPasswordField(), passwordLabelField);
		}

		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, passwordFieldDefinition,
				Login.PasswordField.class);
		passwordFieldDefinition.setPassword(true);

		TerminalPosition errorFieldPosition = loginScreenFact.getErrorPosition();
		if (errorFieldPosition != null) {
			SimpleScreenFieldDefinition screenFieldDefinition = new SimpleScreenFieldDefinition();
			screenFieldDefinition.setName(errorFieldName);
			ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, screenFieldDefinition,
					Login.ErrorField.class);
			screenFieldDefinition.setPosition(errorFieldPosition);
			screenEntityDefinition.getFieldsDefinitions().put(errorFieldName, screenFieldDefinition);
		}

	}

	@Override
	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof LoginScreenFact);
	}

	public void setUserFieldName(String userFieldName) {
		this.userFieldName = userFieldName;
	}

	public void setPasswordFieldName(String passwordFieldName) {
		this.passwordFieldName = passwordFieldName;
	}

	public void setErrorFieldName(String errorFieldName) {
		this.errorFieldName = errorFieldName;
	}
}
