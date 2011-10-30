package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.modules.login.Login;

@ScreenEntity(screenType = Login.LoginScreen.class, identifiers = { @Identifier(row = 1, column = 36, value = "Sign On") })
public class SignOn {

	@FieldMapping(row = 6, column = 53, fieldType = Login.UserField.class, editable = true)
	private String user;

	@FieldMapping(row = 7, column = 53, fieldType = Login.PasswordField.class, editable = true)
	private String password;

	@FieldMapping(row = 24, column = 2, fieldType = Login.ErrorField.class)
	private String error;
}
