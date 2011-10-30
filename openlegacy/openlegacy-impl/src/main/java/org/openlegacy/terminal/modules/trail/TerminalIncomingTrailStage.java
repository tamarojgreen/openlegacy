package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.TrailStage;
import org.openlegacy.terminal.TerminalScreen;

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
