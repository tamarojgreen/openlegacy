package org.openlegacy.adapter;

import org.openlegacy.adapter.terminal.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.trail.TerminalIncomingTrailStage;
import org.openlegacy.terminal.trail.TerminalOutgoingTrailStage;
import org.openlegacy.trail.SessionTrail;

public class SessionTrailModule extends TerminalSessionModuleAdapter {

	private SessionTrail sessionTrail;

	public SessionTrail getSessionTrail() {
		return sessionTrail;
	}

	@Override
	public void beforeSendAction(TerminalConnection terminalConnection, TerminalSendAction terminalSendAction) {
		sessionTrail.appendStage(new TerminalOutgoingTrailStage(terminalConnection.getSnapshot(), terminalSendAction));
	}

	@Override
	public void afterSendAction(TerminalConnection terminalConnection) {
		sessionTrail.appendStage(new TerminalIncomingTrailStage(terminalConnection.getSnapshot()));
	}

	public void setSessionTrail(SessionTrail sessionTrail) {
		this.sessionTrail = sessionTrail;
	}
}
