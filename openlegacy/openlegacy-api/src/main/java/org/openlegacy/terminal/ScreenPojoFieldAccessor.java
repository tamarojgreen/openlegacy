package org.openlegacy.terminal;

import org.openlegacy.HostEntityFieldAccessor;

/**
 * An interface for accessing POJO's marked with @ScreenEntity, @ScreenPart
 * 
 */
public interface ScreenPojoFieldAccessor extends HostEntityFieldAccessor {

	public abstract void setTerminalField(String fieldName, TerminalField terminalField);

	public abstract void setTerminalScreen(TerminalScreen terminalScreen);

}