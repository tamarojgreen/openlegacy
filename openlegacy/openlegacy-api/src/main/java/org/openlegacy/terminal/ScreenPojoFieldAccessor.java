package org.openlegacy.terminal;

import org.openlegacy.HostEntityFieldAccessor;

/**
 * An interface for accessing POJO's marked with @ScreenEntity, @ScreenPart
 * 
 */
public interface ScreenPojoFieldAccessor extends HostEntityFieldAccessor {

	void setFocusField(String fieldName);

	void setTerminalField(String fieldName, TerminalField terminalField);

	void setTerminalScreen(TerminalScreen terminalScreen);

}