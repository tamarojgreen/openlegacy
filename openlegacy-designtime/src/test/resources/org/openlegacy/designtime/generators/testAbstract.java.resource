package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenEntitySuperClass;
import org.openlegacy.annotations.screen.ScreenField;

@ScreenEntitySuperClass(supportTerminalData=true)
public class AbstractScreen {

	@ScreenField(row=24,column=2)
	private String message;
	
}
