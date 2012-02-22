package org.openlegacy.designtime.terminal.analyzer.modules.login;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.terminal.TerminalField;

public class LoginScreenFact implements ScreenFact {

	private TerminalField userField;
	private TerminalField userLabelField;
	private TerminalField passwordField;
	private TerminalField passwordLabelField;
	private TerminalField errorField;

	public LoginScreenFact(TerminalField userField, TerminalField userLabelField, TerminalField passwordField,
			TerminalField passwordLabelField, TerminalField errorField) {
		this.userField = userField;
		this.userLabelField = userLabelField;
		this.passwordField = passwordField;
		this.passwordLabelField = passwordLabelField;
		this.errorField = errorField;
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

	public TerminalField getErrorField() {
		return errorField;
	}
}
