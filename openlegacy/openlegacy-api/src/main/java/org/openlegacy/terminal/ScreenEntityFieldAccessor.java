package org.openlegacy.terminal;

import org.openlegacy.FieldFormatter;
import org.openlegacy.HostEntityFieldAccessor;

public interface ScreenEntityFieldAccessor extends HostEntityFieldAccessor {

	public abstract void setTerminalField(String fieldName, TerminalField terminalField, FieldFormatter fieldFormatter);

	public abstract void setTerminalScreen(TerminalScreen terminalScreen);

}