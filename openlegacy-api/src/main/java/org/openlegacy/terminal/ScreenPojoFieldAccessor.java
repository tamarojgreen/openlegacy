package org.openlegacy.terminal;

import org.openlegacy.EntityFieldAccessor;

/**
 * An interface for accessing POJO's marked with @ScreenEntity, @ScreenPart
 * 
 */
public interface ScreenPojoFieldAccessor extends EntityFieldAccessor {

	void setFocusField(String fieldName);

	void setTerminalField(String fieldName, TerminalField terminalField);

	void setTerminalSnapshot(TerminalSnapshot terminalSnapshot);

}