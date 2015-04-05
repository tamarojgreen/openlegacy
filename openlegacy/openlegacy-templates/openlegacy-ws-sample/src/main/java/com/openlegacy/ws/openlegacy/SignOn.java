package com.openlegacy.ws.openlegacy;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.modules.login.Login.ErrorField;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.login.Login.PasswordField;
import org.openlegacy.modules.login.Login.UserField;

@ScreenEntity(screenType=LoginEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 36, value = "Sign On             ") 
				})
public class SignOn {
    
	
	@ScreenField(row = 6, column = 53, endColumn= 62 ,labelColumn= 17 ,editable= true ,fieldType=UserField.class ,displayName = "User")
    private String user;
	
	@ScreenField(row = 7, column = 53, endColumn= 62 ,labelColumn= 17 ,password= true ,editable= true ,fieldType=PasswordField.class ,displayName = "Password")
    private String password;
	
	@ScreenField(row = 10, column = 53, defaultValue = "LAN8XAPPO", endColumn = 62, labelColumn = 17, editable = true, displayName = "Current library", sampleValue = "LAN8XAPPO")
    private String currentLibrary;
	
	@ScreenField(row = 24, column = 2 ,fieldType=ErrorField.class)
    private String errorMessage;


    


 
}
