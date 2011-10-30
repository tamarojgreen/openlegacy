package org.openlegacy.terminal.support;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.modules.HostSessionModule;
import org.openlegacy.support.AbstractHostSession;
import org.openlegacy.terminal.CursorContainer;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionModule;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;

/**
 * A default session class exposes screenEntity building and sending
 * 
 * 
 */
public class DefaultTerminalSession extends AbstractHostSession implements TerminalSession {

	@Autowired
	private ScreenEntityBinder screenEntityBinder;

	private TerminalConnection terminalConnection;

	private Object entity;

	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> hostEntity) throws HostEntityNotFoundException {

		// check if the entity matched the cached entity
		if (entity == null || entity.getClass() != hostEntity) {
			TerminalScreen hostScreen = getSnapshot();
			entity = screenEntityBinder.buildScreenEntity(hostEntity, hostScreen);
		}
		return (T)entity;
	}

	public Object getEntity() {
		if (entity == null) {
			TerminalScreen hostScreen = getSnapshot();
			entity = screenEntityBinder.buildScreenEntity(hostScreen);
		}
		return entity;
	}

	public TerminalSession doAction(HostAction hostAction, Object screenEntityInstance) {

		entity = null;

		Map<ScreenPosition, String> fieldValues = screenEntityBinder.buildSendFields(getSnapshot(), screenEntityInstance);

		ScreenPosition cursorPosition = null;
		if (screenEntityInstance instanceof CursorContainer) {
			cursorPosition = ((CursorContainer)screenEntityInstance).getCursorPosition();
		}
		TerminalSendAction terminalSendAction = new SimpleTerminalSendAction(fieldValues, hostAction, cursorPosition);

		notifyModulesBeforeSend(terminalSendAction);
		terminalConnection.doAction(terminalSendAction);
		notifyModulesAfterSend();

		return this;
	}

	private void notifyModulesBeforeSend(TerminalSendAction terminalSendAction) {
		Collection<? extends HostSessionModule> modulesList = getSessionModules().getModules();
		for (HostSessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalSessionModule) {
				((TerminalSessionModule)sessionModule).beforeSendAction(terminalConnection, terminalSendAction);
			}
		}
	}

	private void notifyModulesAfterSend() {
		Collection<? extends HostSessionModule> modulesList = getSessionModules().getModules();
		for (HostSessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalSessionModule) {
				((TerminalSessionModule)sessionModule).afterSendAction(terminalConnection);
			}
		}
	}

	public TerminalScreen getSnapshot() {
		return terminalConnection.getSnapshot();
	}

	public Object getDelegate() {
		return terminalConnection.getDelegate();
	}

	public void setTerminalConnection(TerminalConnection terminalConnection) {
		this.terminalConnection = terminalConnection;
	}

}
