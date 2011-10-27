package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@ScreenEntity(identifiers = { @Identifier(row = 1, column = 36, value = "Sign On") })
public class SignOn {

	@FieldMapping(row = 6, column = 53)
	private String user;
	@FieldMapping(row = 7, column = 53)
	private String password;
}
