package org.openlegacy.terminal.modules.trail;

import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.trail.TrailStage;

public class TerminalIncomingTrailStage implements TrailStage {

	private TerminalScreen terminalScreen;

	public TerminalIncomingTrailStage(TerminalScreen terminalScreen) {
		this.terminalScreen = terminalScreen;
	}

	public StageType getStageType() {
		return StageType.INCOMING;
	}

	public TerminalScreen getTerminalScreen() {
		return terminalScreen;
	}
}
