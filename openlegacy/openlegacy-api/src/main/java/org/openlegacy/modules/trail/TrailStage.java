package org.openlegacy.modules.trail;

public interface TrailStage {

	public enum StageType {
		INCOMING,
		OUTGOING
	}

	StageType getStageType();
}
