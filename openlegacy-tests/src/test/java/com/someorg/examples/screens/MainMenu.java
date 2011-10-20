package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity(identifiers = { @Identifier(row = 1, column = 37, value = "Demo Environment") })
public class MainMenu {

	@FieldMapping(row = 21, column = 74, updatable = true)
	private String company;
	@FieldMapping(row = 21, column = 8, updatable = true)
	private String selection;
}
