package org.openlegacy.adapter.terminal;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.ScreenEntitySender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * An abstract class which should extended by terminal vendors which exposes screenEntity building and sending
 * 
 * 
 */
public abstract class TerminalSessionAdapter implements TerminalSession {

	@Autowired
	private ScreenEntityBinder screenEntityBinder;

	@Autowired
	private ScreenEntitySender screenEntitySender;

	public <T> T getEntity(Class<T> hostEntity) throws HostEntityNotFoundException {
		TerminalScreen hostScreen = getSnapshot();

		return screenEntityBinder.buildScreenEntity(hostEntity, hostScreen);
	}

	public TerminalSession doAction(HostAction hostAction) {
		doAction(hostAction, null, null);
		return this;
	}

	public TerminalSession doAction(HostAction hostAction, Object screenEntityInstance) {
		return doAction(hostAction, screenEntityInstance, null);
	}

	public TerminalSession doAction(HostAction hostAction, Object screenEntityInstance, ScreenPosition cursorPosition) {

		Map<ScreenPosition, String> fieldValues = screenEntityBinder.buildSendFields(getSnapshot(), screenEntityInstance);

		screenEntitySender.perform(this, fieldValues, hostAction, cursorPosition);
		return this;
	}

}
