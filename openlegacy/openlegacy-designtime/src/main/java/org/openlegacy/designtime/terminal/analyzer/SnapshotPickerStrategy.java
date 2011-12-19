package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.Snapshot;

import java.util.Set;

public interface SnapshotPickerStrategy<S extends Snapshot> {

	S pickRepresenter(Set<S> group);
}
