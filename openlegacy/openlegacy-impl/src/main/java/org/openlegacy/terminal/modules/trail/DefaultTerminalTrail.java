package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.modules.trail.TrailStage;

import java.util.LinkedList;
import java.util.List;

public class DefaultTerminalTrail implements SessionTrail {

	private LinkedList<TrailStage> stages = new LinkedList<TrailStage>();

	private int historyCount = 5;

	public List<TrailStage> getStages() {
		return stages;
	}

	public void appendStage(TrailStage stage) {
		stages.add(stage);

		if (stages.size() > historyCount) {
			stages.removeFirst();
		}
	}

	public int getHistoryCount() {
		return historyCount;
	}

	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}
}
