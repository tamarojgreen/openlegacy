package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity
public class PartScreen {

	private PartScreen1 partScreen1;

	@ScreenPart
	public static class PartScreen1{
	
		@ScreenDescriptionField(column = 12)
		@ScreenField(column = 2, row= 2)
		private String field1;
	}
}
