package org.openlegacy.trail;

public interface TrailStage {

	public enum StageType {
		INCOMING,
		OUTGOING
	}

	StageType getStageType();
}
