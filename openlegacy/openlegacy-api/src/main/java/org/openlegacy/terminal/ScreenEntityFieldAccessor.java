package org.openlegacy.terminal;

import org.openlegacy.HostEntityFieldAccessor;

public interface ScreenEntityFieldAccessor extends HostEntityFieldAccessor {

	public abstract void setTerminalField(String fieldName, TerminalField terminalField);

	public abstract void setTerminalScreen(TerminalScreen terminalScreen);

}