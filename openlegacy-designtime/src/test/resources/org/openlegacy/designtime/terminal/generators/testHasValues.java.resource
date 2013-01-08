package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;

@ScreenEntity
public class ScreenWithValues {

	@ScreenFieldValues(sourceScreenEntity = SourceScreen.class, collectAll = false)
	private String someField;
}
