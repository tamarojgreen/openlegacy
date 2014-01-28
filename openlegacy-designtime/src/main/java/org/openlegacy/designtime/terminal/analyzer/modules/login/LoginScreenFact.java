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
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;

public class LoginScreenFact implements ScreenFact {

	private TerminalField userField;
	private TerminalField userLabelField;
	private TerminalField passwordField;
	private TerminalField passwordLabelField;
	private TerminalPosition errorPosition;

	public LoginScreenFact(TerminalField userField, TerminalField userLabelField, TerminalField passwordField,
			TerminalField passwordLabelField, TerminalPosition errorPosition) {
		this.userField = userField;
		this.userLabelField = userLabelField;
		this.passwordField = passwordField;
		this.passwordLabelField = passwordLabelField;
		this.errorPosition = errorPosition;
	}

	public TerminalField getUserField() {
		return userField;
	}

	public TerminalField getUserLabelField() {
		return userLabelField;
	}

	public TerminalField getPasswordField() {
		return passwordField;
	}

	public TerminalField getPasswordLabelField() {
		return passwordLabelField;
	}

	public TerminalPosition getErrorPosition() {
		return errorPosition;
	}

	public String getPasswordLabel() {
		if (passwordLabelField != null) {
			return passwordLabelField.getValue();
		}
		return null;
	}
}
