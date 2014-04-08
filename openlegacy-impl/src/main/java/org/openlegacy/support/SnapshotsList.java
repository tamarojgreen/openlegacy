package org.openlegacy.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnapshotsList<S> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<SnapshotInfo<S>> snapshots = new ArrayList<SnapshotInfo<S>>();
	private int current;

	public void add(S snapshot, int indexInSession) {
		// don't put duplicates
		if (!snapshots.contains(snapshot)) {
			snapshots.add(new SnapshotInfo<S>(snapshot, indexInSession));
		}
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public SnapshotInfo<S> getCurrent() {
		return snapshots.get(current);
	}

	public void next() {
		if (current < snapshots.size() - 1) {
			current++;
		} else {
			current = 0;
		}
	}

	public static class SnapshotInfo<S> implements Serializable {

		private static final long serialVersionUID = 1L;

		private S snapshot;
		private int indexInSession;

		public SnapshotInfo(S snapshot, int indexInSession) {
			this.snapshot = snapshot;
			this.indexInSession = indexInSession;
		}

		public int getIndexInSession() {
			return indexInSession;
		}

		public S getSnapshot() {
			return snapshot;
		}
	}
}
