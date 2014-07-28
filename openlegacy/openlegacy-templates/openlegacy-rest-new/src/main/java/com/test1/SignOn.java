package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.modules.login.Login.LoginEntity;  
import org.openlegacy.modules.login.Login.UserField;  
import org.openlegacy.modules.login.Login.PasswordField;  
import org.openlegacy.modules.login.Login.ErrorField;  

@ScreenEntity(screenType=LoginEntity.class)
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 1, column = 23, value = "Sign On"), 
				@Identifier(row = 6, column = 17, value = "User  . . . . . . . . . . . . . ."), 
				@Identifier(row = 7, column = 17, value = "Password  . . . . . . . . . . . .") 
				})
public class SignOn {
    
	
	@ScreenField(row = 6, column = 53, endColumn= 62 ,labelColumn= 17 ,editable= true ,fieldType=UserField.class ,displayName = "User")
    private String user;
	
	@ScreenField(row = 7, column = 53, endColumn= 62 ,labelColumn= 17 ,password= true ,editable= true ,fieldType=PasswordField.class ,displayName = "Password")
    private String password;
	
	@ScreenField(row = 24, column = 2 ,fieldType=ErrorField.class)
    private String errorMessage;


    


 
}
