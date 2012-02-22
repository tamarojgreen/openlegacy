package org.openlegacy.designtime.analyzer;

import org.openlegacy.Snapshot;

public interface SnapshotsSimilarityChecker<S extends Snapshot> {

	public int similarityPercent(S snapshot1, S snapshot2);
}
