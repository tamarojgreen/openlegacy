package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.modules.login.LoginModule;

@ScreenEntity(supportTerminalData = true, screenType = LoginModule.LoginScreen.class, identifiers = { @Identifier(row = 1, column = 36, value = "Sign On") })
public class SignOn {

	@FieldMapping(row = 6, column = 53, fieldType = LoginModule.UserField.class, editable = true)
	private String user;

	@FieldMapping(row = 7, column = 53, fieldType = LoginModule.PasswordField.class, editable = true)
	private String password;

	@FieldMapping(row = 24, column = 2, fieldType = LoginModule.ErrorField.class)
	private String error;
}
