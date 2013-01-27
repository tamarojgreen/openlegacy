package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenListField;

import java.util.List;

import junit.framework.Assert;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 2, column = 27, value = "Items") })
public class ListFieldEntity implements org.openlegacy.terminal.ScreenEntity {

	@ScreenListField(fieldLength = 9, count = 3, gaps = { 10, 10 })
	@ScreenField(row = 8, column = 25)
	private List<String> toysList;

	public List<String> getToysList() {
		return toysList;
	}

	public void setToysList(List<String> toysList) {
		Assert.assertEquals(toysList.size(), this.toysList.size());
		this.toysList = toysList;
	}

	@ScreenListField(fieldLength = 9, count = 3, gaps = { 10 })
	@ScreenField(row = 9, column = 25)
	private String[] toysArray;

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {

	}

	public String[] getToysArray() {
		return toysArray;
	}

	public void setToysArray(String[] toysArray) {
		this.toysArray = toysArray;
	}

}
