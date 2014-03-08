package com.someorg.examples.screens;

import org.openlegacy.annotations.screen.FieldMapping;
import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.ServiceOutput;

@ServiceOutput
@ScreenEntity()
public class ItemDetails {

	private Integer itemNumber;
	private String description;

}
