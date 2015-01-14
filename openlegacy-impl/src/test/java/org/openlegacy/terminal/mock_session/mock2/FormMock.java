package org.openlegacy.terminal.mock_session.mock2;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.terminal.support.AbstractScreenEntity;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(column = 15, row = 1, value = "** Form **") })
@ScreenNavigation(accessedFrom = TableMock.class,drilldownValue="1")
public class FormMock extends AbstractScreenEntity {

	@ScreenField(column = 15, row = 4,editable=true,key=true)
	private String field1;

	public String getField1() {
		return field1;
	}
	
	public void setField1(String field1) {
		this.field1 = field1;
	}
}
