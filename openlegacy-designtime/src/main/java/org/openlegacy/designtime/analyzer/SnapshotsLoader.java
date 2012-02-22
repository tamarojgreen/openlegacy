package org.openlegacy.designtime.analyzer;

import org.openlegacy.Snapshot;
import org.openlegacy.exceptions.UnableToLoadSnapshotException;

import java.util.List;

public interface SnapshotsLoader<S extends Snapshot> {

	/**
	 * Load snapshots from the specified root. If no fileNames are specified, all files are loaded
	 * 
	 * @throws UnableToLoadSnapshotException
	 */
	List<S> loadSnapshots(String root, String... fileNames) throws UnableToLoadSnapshotException;

	S load(String path) throws UnableToLoadSnapshotException;
}
