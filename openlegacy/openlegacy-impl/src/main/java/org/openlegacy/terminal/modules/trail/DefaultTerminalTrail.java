package org.openlegacy.terminal.modules.trail;

import org.openlegacy.trail.SessionTrail;
import org.openlegacy.trail.TrailStage;

import java.util.ArrayList;
import java.util.List;

public class DefaultTerminalTrail implements SessionTrail {

	private List<TrailStage> stages = new ArrayList<TrailStage>();

	public List<TrailStage> getStages() {
		return stages;
	}

	public void appendStage(TrailStage stage) {
		stages.add(stage);
	}
}
