package org.openlegacy.terminal.spi;

import org.openlegacy.exceptions.HostEntityNotAccessibleException;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;

import java.util.Map;

/**
 * Define a binder api for building a screenEntity instance from a terminalScreen and collecting fields from a given screenEntity
 * 
 */
public interface ScreenEntityBinder {

	<T> T buildScreenEntity(Class<T> screenEntity, TerminalScreen hostScreen) throws HostEntityNotFoundException,
			HostEntityNotAccessibleException;

	Map<ScreenPosition, String> buildSendFields(TerminalScreen terminalScreen, Object screenEntityInstance);

}
