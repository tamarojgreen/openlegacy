package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailModule;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.TerminalIncomingSnapshot;
import org.openlegacy.terminal.support.TerminalOutgoingSnapshot;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;

public class HostSessionTrailModuleImpl extends TerminalSessionModuleAdapter implements TrailModule {

	private SessionTrail<TerminalSnapshot> sessionTrail;

	public SessionTrail<TerminalSnapshot> getSessionTrail() {
		return sessionTrail;
	}

	@Override
	public void beforeSendAction(TerminalConnection terminalConnection, TerminalSendAction terminalSendAction) {
		sessionTrail.appendSnapshot(new TerminalOutgoingSnapshot(terminalConnection.getSnapshot(), terminalSendAction));
	}

	@Override
	public void afterSendAction(TerminalConnection terminalConnection) {
		sessionTrail.appendSnapshot(new TerminalIncomingSnapshot(terminalConnection.getSnapshot()));
	}

	public void setSessionTrail(SessionTrail<TerminalSnapshot> sessionTrail) {
		this.sessionTrail = sessionTrail;
	}
}
