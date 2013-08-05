package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenListField;

import java.util.List;

import junit.framework.Assert;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 21, value = "integer") })
public class ListNumEntity implements org.openlegacy.terminal.ScreenEntity {

	@ScreenListField(fieldLength = 9, count = 3, gaps = { 10, 10 })
	@ScreenField(row = 8, column = 25)
	private List<Integer> intList;

	public List<Integer> getIntList() {
		return intList;
	}

	public void setIntList(List<Integer> intList) {
		Assert.assertEquals(intList.size(), this.intList.size());
		this.intList = intList;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {

	}

}
