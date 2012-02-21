package org.openlegacy.terminal.mock_session.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.support.AbstractScreenEntity;
import org.springframework.stereotype.Component;

@Component
@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(column = 3, row = 1, value = "Field A:") })
public class Screen1 extends AbstractScreenEntity {

	@ScreenField(row = 1, column = 3, editable = true)
	private String fieldA;

	public String getFieldA() {
		return fieldA;
	}

	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}
}
