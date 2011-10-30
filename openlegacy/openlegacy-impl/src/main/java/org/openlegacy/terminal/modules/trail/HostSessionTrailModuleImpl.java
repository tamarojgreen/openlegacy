package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;

public class HostSessionTrailModuleImpl extends TerminalSessionModuleAdapter implements Trail{

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
