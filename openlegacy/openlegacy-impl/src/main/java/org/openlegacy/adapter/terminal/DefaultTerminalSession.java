package org.openlegacy.adapter.terminal;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.CursorContainer;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.SimpleTerminalSendAction;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.trail.SessionTrail;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * A default session class exposes screenEntity building and sending
 * 
 * 
 */
public class DefaultTerminalSession implements TerminalSession {

	@Autowired
	private ScreenEntityBinder screenEntityBinder;

	@Autowired
	private TerminalConnection terminalConnection;

	@Autowired
	private SessionTrail sessionTrail;

	public <T> T getEntity(Class<T> hostEntity) throws HostEntityNotFoundException {
		TerminalScreen hostScreen = getSnapshot();

		return screenEntityBinder.buildScreenEntity(hostEntity, hostScreen);
	}

	public TerminalSession doAction(HostAction hostAction, Object screenEntityInstance) {

		Map<ScreenPosition, String> fieldValues = screenEntityBinder.buildSendFields(getSnapshot(), screenEntityInstance);

		ScreenPosition cursorPosition = null;
		if (screenEntityInstance instanceof CursorContainer) {
			cursorPosition = ((CursorContainer)screenEntityInstance).getCursorPosition();
		}
		TerminalSendAction terminalSendAction = new SimpleTerminalSendAction(fieldValues, hostAction, cursorPosition);

		terminalConnection.doAction(terminalSendAction);

		return this;
	}

	public SessionTrail getSessionTrail() {
		return sessionTrail;
	}

	public TerminalScreen getSnapshot() {
		return terminalConnection.getSnapshot();
	}

	public Object getDelegate() {
		return terminalConnection.getDelegate();
	}

}
