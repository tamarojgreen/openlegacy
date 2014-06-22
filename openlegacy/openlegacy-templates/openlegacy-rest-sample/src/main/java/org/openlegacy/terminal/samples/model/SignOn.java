package org.openlegacy.terminal.samples.model;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.login.Login.ErrorField;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.login.Login.PasswordField;
import org.openlegacy.modules.login.Login.UserField;

@ScreenEntity(screenType = LoginEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 23, value = "             Sign On             "),
		@Identifier(row = 4, column = 48, value = "Display . . . . . :"),
		@Identifier(row = 6, column = 17, value = "User  . . . . . . . . . . . . . .") })
public class SignOn {

	@ScreenField(row = 2, column = 70, endColumn = 77, labelColumn = 48, displayName = "System", sampleValue = "S44R5550")
	private String system;

	@ScreenField(row = 3, column = 70, endColumn = 79, labelColumn = 48, displayName = "Subsystem", sampleValue = "QBASE")
	private String subsystem;

	@ScreenField(row = 4, column = 70, endColumn = 79, labelColumn = 48, displayName = "Display", sampleValue = "QPADEV00RT")
	private String display;

	@ScreenField(row = 6, column = 53, endColumn = 62, labelColumn = 17, editable = true, fieldType = UserField.class, displayName = "User", sampleValue = "user")
	private String user;

	@ScreenField(row = 7, column = 53, endColumn = 62, labelColumn = 17, editable = true, password = true, fieldType = PasswordField.class, displayName = "Password", sampleValue = "pwd")
	private String password;

	@ScreenField(row = 8, column = 53, endColumn = 62, labelColumn = 17, editable = true, displayName = "Program/procedure")
	private String programprocedure;

	@ScreenField(row = 9, column = 53, endColumn = 62, labelColumn = 17, editable = true, displayName = "Menu")
	private String menu;

	@ScreenField(row = 10, column = 53, endColumn = 62, labelColumn = 17, editable = true, displayName = "Current library")
	private String currentLibrary;

	@ScreenField(row = 24, column = 1, endColumn = 38, fieldType = ErrorField.class, displayName = "")
	private String errorMessage;

}
