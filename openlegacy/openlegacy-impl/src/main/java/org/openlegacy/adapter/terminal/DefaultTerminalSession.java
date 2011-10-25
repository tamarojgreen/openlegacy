package org.openlegacy.adapter.terminal;

import org.openlegacy.HostAction;
import org.openlegacy.exceptions.HostEntityNotFoundException;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.spi.ScreenEntityBinder;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.trail.TerminalIncomingTrailStage;
import org.openlegacy.terminal.trail.TerminalOutgoingTrailStage;
import org.openlegacy.trail.SessionTrail;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * A default session class exposes screenEntity building and sending
 * 
 * 
 */
public class DefaultTerminalSession implements TerminalSession, InitializingBean {

	@Autowired
	private ScreenEntityBinder screenEntityBinder;

	private TerminalConnection terminalConnection;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SessionTrail sessionTrail;

	public <T> T getEntity(Class<T> hostEntity) throws HostEntityNotFoundException {
		TerminalScreen hostScreen = getSnapshot();

		return screenEntityBinder.buildScreenEntity(hostEntity, hostScreen);
	}

	public TerminalSession doAction(HostAction hostAction, Object screenEntityInstance, ScreenPosition cursorPosition) {

		Map<ScreenPosition, String> fieldValues = screenEntityBinder.buildSendFields(getSnapshot(), screenEntityInstance);

		TerminalSendAction terminalSendAction = new TerminalSendAction(fieldValues, hostAction, cursorPosition);

		sessionTrail.appendStage(new TerminalOutgoingTrailStage(getSnapshot(), terminalSendAction));
		terminalConnection.doAction(terminalSendAction);
		sessionTrail.appendStage(new TerminalIncomingTrailStage(getSnapshot()));

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

	public void afterPropertiesSet() throws Exception {
		TerminalConnectionFactory terminalConnectionFactory = applicationContext.getBean(TerminalConnectionFactory.class);
		terminalConnection = terminalConnectionFactory.getConnection();
	}
}
