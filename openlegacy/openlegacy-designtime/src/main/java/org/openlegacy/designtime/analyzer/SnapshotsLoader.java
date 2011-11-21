package org.openlegacy.designtime.analyzer;

import org.openlegacy.Snapshot;
import org.openlegacy.exceptions.UnableToLoadSnapshotException;

import java.util.List;

public interface SnapshotsLoader<S extends Snapshot> {

	List<S> loadAll(String root) throws UnableToLoadSnapshotException;
}
