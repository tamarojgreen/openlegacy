package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity
public class DescriptionFieldScreen {

	@ScreenDescriptionField(column=5)
	@ScreenField(row=1,column=1)
	private String someField;
}
