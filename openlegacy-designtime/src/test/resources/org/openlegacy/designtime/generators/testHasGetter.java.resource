package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity(supportTerminalData=true)
public class SignOn {

	private String user;
	private String password;

	public String getUser() {
		return "";
	}
}
