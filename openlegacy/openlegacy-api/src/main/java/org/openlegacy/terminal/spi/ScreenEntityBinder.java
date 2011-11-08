package org.openlegacy.terminal.spi;

import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;

import java.util.List;

/**
 * Define a binder API for building a screen entity from a terminalScreen and collecting fields from a given screenEntity
 * 
 */
public interface ScreenEntityBinder {

	Object buildScreenEntity(TerminalScreen hostScreen);

	<T> T buildScreenEntity(Class<T> screenEntityClass, TerminalScreen hostScreen) throws HostEntityNotFoundException,
			ScreenEntityNotAccessibleException;

	List<TerminalField> buildSendFields(TerminalScreen terminalScreen, Object screenEntity);

}
