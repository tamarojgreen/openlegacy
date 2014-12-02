package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity(supportTerminalData=true)
public class SignOn {

	private static String staticString = "staticString";
	private final String finalString = "finalString";

	private String user;
	private String password;

}
