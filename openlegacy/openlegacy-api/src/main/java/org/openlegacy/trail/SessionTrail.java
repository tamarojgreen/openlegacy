package org.openlegacy.trail;

import java.util.List;

public interface SessionTrail {

	List<TrailStage> getStages();

	void appendStage(TrailStage stage);
}
