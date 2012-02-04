package org.openlegacy.terminal.layout.mock;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.modules.login.Login;
import org.springframework.stereotype.Component;

@ScreenEntity
@Component
public class Screen1 implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 2, column = 70, endColumn = 77, displayName = "Field 1A", sampleValue = "111AAA")
	private String field1A;

	@ScreenField(row = 3, column = 70, endColumn = 79, displayName = "Field 1BB", sampleValue = "111BBB")
	private String field1B;

	@ScreenField(row = 4, column = 70, endColumn = 79, displayName = "Field 1CCC", sampleValue = "111CCC")
	private String field1C;

	@ScreenField(row = 6, column = 53, editable = true, endColumn = 63)
	private String field2A;

	@ScreenField(row = 7, column = 53, editable = true)
	private String field2B;

	@ScreenField(row = 8, column = 53, editable = true)
	private String field2C;

	@ScreenField(row = 10, column = 33, endColumn = 43)
	private String field3A;

	@ScreenField(row = 10, column = 53)
	private String field3B;

	public String getField1A() {
		return field1A;
	}

	public String getField1B() {
		return field1B;
	}

	public String getField1C() {
		return field1C;
	}

	public String getField2A() {
		return field2A;
	}

	public String getField2B() {
		return field2B;
	}

	public String getField2C() {
		return field2C;
	}

	public String getField3A() {
		return field3A;
	}

	public String getField3B() {
		return field3B;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

}
