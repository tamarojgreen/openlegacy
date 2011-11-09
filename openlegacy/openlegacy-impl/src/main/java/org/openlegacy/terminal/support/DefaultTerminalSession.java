package org.openlegacy.terminal.support;

import org.openlegacy.CustomHostAction;
import org.openlegacy.HostAction;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.modules.HostSessionModule;
import org.openlegacy.support.AbstractHostSession;
import org.openlegacy.terminal.CursorContainer;
import org.openlegacy.terminal.HostActionMapper;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalConnectionListener;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.SessionNavigator;
import org.openlegacy.terminal.spi.TerminalSendAction;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * A default session class exposes screenEntity building and sending
 * 
 * 
 */
public class DefaultTerminalSession extends AbstractHostSession implements TerminalSession {

	@Inject
	private ScreenEntityBinder screenEntityBinder;

	private TerminalConnectionDelegator terminalConnection;

	private Object entity;

	@Inject
	private SessionNavigator sessionNavigator;

	@Inject
	private HostActionMapper hostActionMapper;

	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> screenEntityClass) throws HostEntityNotFoundException {

		// check if the entity matched the cached entity
		if (entity == null || entity.getClass() != screenEntityClass) {
			sessionNavigator.navigate(this, screenEntityClass);

			TerminalScreen hostScreen = getSnapshot();

			entity = screenEntityBinder.buildScreenEntity(screenEntityClass, hostScreen);
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

	public Object doAction(HostAction hostAction, Object screenEntity) {
		return doActionInner(hostAction, screenEntity);
	}

	public <T> T doAction(HostAction hostAction, Object screenEntity, Class<T> expectedEntity) {
		try {
			@SuppressWarnings("unchecked")
			T object = (T)doActionInner(hostAction, screenEntity);
			return object;
		} catch (ClassCastException e) {
			throw (new ScreenEntityNotAccessibleException(e));
		}

	}

	private Object doActionInner(HostAction hostAction, Object screenEntity) {

		entity = null;

		List<TerminalField> modifiedFields = screenEntityBinder.buildSendFields(getSnapshot(), screenEntity);

		ScreenPosition cursorPosition = null;
		if (screenEntity instanceof CursorContainer) {
			cursorPosition = ((CursorContainer)screenEntity).getCursorPosition();
		}
		if (hostAction instanceof CustomHostAction) {
			((CustomHostAction)hostAction).perform(this);
		} else {
			TerminalSendAction terminalSendAction = new SimpleTerminalSendAction(modifiedFields,
					hostActionMapper.getCommand(hostAction.getClass()), cursorPosition);

			notifyModulesBeforeSend(terminalSendAction);
			terminalConnection.doAction(terminalSendAction);
			notifyModulesAfterSend();
		}

		return getEntity();
	}

	private void notifyModulesBeforeSend(TerminalSendAction terminalSendAction) {
		Collection<? extends HostSessionModule> modulesList = getSessionModules().getModules();
		for (HostSessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).beforeSendAction(terminalConnection, terminalSendAction);
			}
		}
	}

	private void notifyModulesAfterSend() {
		Collection<? extends HostSessionModule> modulesList = getSessionModules().getModules();
		for (HostSessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).afterSendAction(terminalConnection);
			}
		}
	}

	public TerminalScreen getSnapshot() {
		return terminalConnection.getSnapshot();
	}

	public Object getDelegate() {
		return terminalConnection.getDelegate();
	}

	public void setTerminalConnection(TerminalConnectionDelegator terminalConnection) {
		this.terminalConnection = terminalConnection;
	}

	public void disconnect() {
		terminalConnection.disconnect();
	}

	public boolean isConnected() {
		return terminalConnection.isConnected();
	}

}
