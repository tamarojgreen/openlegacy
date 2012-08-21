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
package org.openlegacy.designtime.terminal.analyzer.modules.login;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.analyzer.support.ScreenEntityDefinitionsBuilderUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.utils.ClassUtils;

import javax.inject.Inject;

public class LoginScreenFactProcessor implements ScreenFactProcessor {

	@Inject
	private ScreenEntityDefinitionsBuilderUtils screenEntityDefinitionsBuilderUtils;

	private String defaultPasswordLabel = "Password";

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {

		LoginScreenFact loginScreenFact = (LoginScreenFact)screenFact;

		screenEntityDefinition.setType(Login.LoginEntity.class);
		screenEntityDefinition.getReferredClasses().add(ClassUtils.getImportDeclaration(Login.LoginEntity.class));

		ScreenFieldDefinition userFieldDefinition = screenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
				loginScreenFact.getUserField(), loginScreenFact.getUserLabelField());
		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, userFieldDefinition, Login.UserField.class);

		TerminalField passwordLabelField = loginScreenFact.getPasswordLabelField();
		ScreenFieldDefinition passwordFieldDefinition = null;
		if (passwordLabelField != null) {
			passwordFieldDefinition = screenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
					loginScreenFact.getPasswordField(), passwordLabelField);
		} else {
			passwordFieldDefinition = screenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
					loginScreenFact.getPasswordField(), defaultPasswordLabel);
		}

		ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, passwordFieldDefinition,
				Login.PasswordField.class);
		((SimpleScreenFieldDefinition)passwordFieldDefinition).setPassword(true);

		TerminalField errorField = loginScreenFact.getErrorField();
		if (errorField != null) {
			ScreenFieldDefinition errorFieldDefinition = screenEntityDefinitionsBuilderUtils.addField(screenEntityDefinition,
					errorField, Login.ERROR_MESSAGE_LABEL);
			ScreenEntityDefinitionsBuilderUtils.defineFieldType(screenEntityDefinition, errorFieldDefinition,
					Login.ErrorField.class);
		}

	}

	public boolean accept(ScreenFact screenFact) {
		return (screenFact instanceof LoginScreenFact);
	}

	public void setDefaultPasswordLabel(String defaultPasswordLabel) {
		this.defaultPasswordLabel = defaultPasswordLabel;
	}
}
