package org.openlegacy.terminal.layout.mock;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenPart;
import org.springframework.stereotype.Component;

@ScreenEntity
@Component
public class ScreenForPage implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 22, editable = true)
	private String fldCol12;

	@ScreenField(row = 7, column = 22, editable = true)
	private String fld2Col12;

	@ScreenField(row = 8, column = 22, editable = true)
	private String fld3Col12;

	@ScreenField(row = 10, column = 33, endColumn = 43)
	private String fldRow10;

	@ScreenField(row = 10, column = 53)
	private String fld2Row10;

	@ScreenField(row = 24, column = 20)
	private String orphanFld;

	private ScreenForPagePart screenForPagePart;

	public String getFldCol12() {
		return fldCol12;
	}

	public String getFld2Col12() {
		return fld2Col12;
	}

	public String getFld3Col12() {
		return fld3Col12;
	}

	public String getFldRow10() {
		return fldRow10;
	}

	public String getFld2Row10() {
		return fld2Row10;
	}

	public String getOrphanFld() {
		return orphanFld;
	}

	public ScreenForPagePart getScreenForPagePart() {
		return screenForPagePart;
	}

	public String getFocusField() {
		return null;
	}

	public void setFocusField(String focusField) {}

	@ScreenPart
	@Component
	public static class ScreenForPagePart {

		@ScreenField(row = 15, column = 13, endColumn = 43)
		private String fldRow15;

		@ScreenField(row = 15, column = 33)
		private String fld2Row15;

		public String getFldRow15() {
			return fldRow15;
		}

		public String getFld2Row15() {
			return fld2Row15;
		}
	}
}
