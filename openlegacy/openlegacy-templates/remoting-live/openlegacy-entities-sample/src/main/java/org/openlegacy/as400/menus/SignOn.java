package org.openlegacy.as400.menus;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.login.Login.ErrorField;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.login.Login.PasswordField;
import org.openlegacy.modules.login.Login.UserField;

import java.io.Serializable;

@ScreenEntity(screenType = LoginEntity.class)
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 23, value = "             Sign On             "),
		@Identifier(row = 4, column = 48, value = "Display . . . . . :"),
		@Identifier(row = 6, column = 17, value = "User  . . . . . . . . . . . . . .") })
public class SignOn implements Serializable {

	private static final long serialVersionUID = 1L;

	@ScreenField(row = 2, column = 70, endColumn = 77, labelColumn = 48, displayName = "System", sampleValue = "S1051C6E")
	private String system;

	@ScreenField(row = 3, column = 70, endColumn = 79, labelColumn = 48, displayName = "Subsystem", sampleValue = "QINTER")
	private String subsystem;

	@ScreenField(row = 4, column = 70, endColumn = 79, labelColumn = 48, displayName = "Display", sampleValue = "QPADEV0001")
	private String display;

	@ScreenField(row = 6, column = 53, endColumn = 62, labelColumn = 17, editable = true, fieldType = UserField.class, displayName = "User")
	private String user;

	@ScreenField(row = 7, column = 53, endColumn = 62, labelColumn = 17, password = true, editable = true, fieldType = PasswordField.class, displayName = "Password")
	private String password;

	@ScreenField(row = 8, column = 53, endColumn = 62, labelColumn = 17, editable = true, displayName = "Program/procedure")
	private String programprocedure;

	@ScreenField(row = 9, column = 53, endColumn = 62, labelColumn = 17, editable = true, displayName = "Menu")
	private String menu_;

	@ScreenField(row = 10, column = 53, endColumn = 62, labelColumn = 17, editable = true, displayName = "Current library")
	private String currentLibrary;

	@ScreenField(row = 24, column = 2, fieldType = ErrorField.class, displayName = "")
	private String errorMessage;

}
