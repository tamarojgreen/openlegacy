package org.openlegacy.terminal;

import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;

public interface ScreenEntityFieldAccessor {

	public abstract boolean isReadableProperty(String fieldName);

	public abstract boolean isWritableProperty(String fieldName);

	public abstract void setTerminalField(String fieldName, TerminalField terminalField);

	public abstract void setFieldValue(String fieldName, Object value);

	public abstract void setTerminalScreen(TerminalScreen terminalScreen);

	public abstract Class<?> getFieldType(String fieldName);

	public abstract Object getFieldValue(String fieldName);

}