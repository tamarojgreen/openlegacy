package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 25, value = "Expression Screen title") })
public class ScreenExpressionScreen implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 6, column = 17, endColumn = 21, displayName = "Total A")
	Integer totalA;

	@ScreenField(row = 7, column = 17, endColumn = 21, displayName = "Total B")
	Integer totalB;

	@ScreenField(row = 8, column = 17, expression = "totalA + totalB", displayName = "Combined total expression")
	Integer combinedTotalAandB;

	@ScreenField(row = 10, column = 10, endColumn = 19, expression = "#field.color == 'RED'", displayName = "Error occurred")
	Boolean errorOccurred;

	@ScreenField(row = 11, column = 10, endColumn = 19, expression = "#field.color == 'RED'", displayName = "Error occurred2")
	Boolean errorOccurred2;

	@ScreenField(row = 11, column = 10, endColumn = 19, expression = "/Message: //", displayName = "Error message")
	String message;

	public Boolean getErrorOccurred2() {
		return errorOccurred2;
	}

	public void setErrorOccurred2(Boolean errorOccurred2) {
		this.errorOccurred2 = errorOccurred2;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTotalA() {
		return totalA;
	}

	public void setTotalA(Integer totalA) {
		this.totalA = totalA;
	}

	public Integer getTotalB() {
		return totalB;
	}

	public void setTotalB(Integer totalB) {
		this.totalB = totalB;
	}

	public Integer getCombinedTotalAandB() {
		return combinedTotalAandB;
	}

	public void setCombinedTotalAandB(Integer combinedTotalAandB) {
		this.combinedTotalAandB = combinedTotalAandB;
	}

	public Boolean getErrorOccurred() {
		return errorOccurred;
	}

	public void setErrorOccurred(Boolean errorOccurred) {
		this.errorOccurred = errorOccurred;
	}

	@Override
	public String getFocusField() {
		return null;
	}

	@Override
	public void setFocusField(String focusField) {}

	@Override
	@SuppressWarnings("unchecked")
	public List<TerminalActionDefinition> getActions() {
		return Collections.EMPTY_LIST;
	}
}
