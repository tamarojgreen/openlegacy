package org.openlegacy.terminal.spi;

import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;

import java.util.Map;

/**
 * Define a binder API for building a screen entity from a terminalScreen and collecting fields from a given screenEntity
 * 
 */
public interface ScreenEntityBinder {

	Object buildScreenEntity(TerminalScreen hostScreen);

	<T> T buildScreenEntity(Class<T> screenEntityClass, TerminalScreen hostScreen) throws HostEntityNotFoundException,
			HostEntityNotAccessibleException;

	Map<ScreenPosition, String> buildSendFields(TerminalScreen terminalScreen, Object screenEntity);

}
