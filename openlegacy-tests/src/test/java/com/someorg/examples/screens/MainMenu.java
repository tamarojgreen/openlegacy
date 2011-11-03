package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenIdentifiers;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 37, value = "Demo Environment") })
public class MainMenu {

	@FieldMapping(row = 21, column = 74, editable = true)
	private String company;
	@FieldMapping(row = 21, column = 8, editable = true)
	private String selection;
}
