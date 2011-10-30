package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.TrailStage;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.spi.TerminalSendAction;

public class TerminalOutgoingTrailStage implements TrailStage {

	private TerminalScreen terminalScreen;
	private TerminalSendAction terminalSendAction;

	public TerminalOutgoingTrailStage(TerminalScreen terminalScreen, TerminalSendAction terminalSendAction) {
		this.terminalScreen = terminalScreen;
		this.terminalSendAction = terminalSendAction;
	}

	public StageType getStageType() {
		return StageType.OUTGOING;
	}

	public TerminalScreen getTerminalScreen() {
		return terminalScreen;
	}

	public TerminalSendAction getTerminalSendAction() {
		return terminalSendAction;
	}
}
