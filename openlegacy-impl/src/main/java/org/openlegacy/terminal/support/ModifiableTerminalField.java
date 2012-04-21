package org.openlegacy.terminal.support;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;

public interface ModifiableTerminalField extends TerminalField {

	void setPosition(TerminalPosition terminalPosition);

	void setEndPosition(TerminalPosition terminalPosition);

	void setValue(String value, boolean modified);

	void setLength(int length);

}
