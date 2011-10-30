package org.openlegacy.modules.trail;

import java.util.List;

public interface SessionTrail {

	List<TrailStage> getStages();

	void appendStage(TrailStage stage);
}
