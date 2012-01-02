package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;

import java.util.List;

/**
 * Defines a session trail. A session trail is compound of multiple <code>Snapshot</code>
 * 
 * @param <S>
 *            The snapshot type
 */
public interface SessionTrail<S extends Snapshot> {

	List<S> getSnapshots();

	void appendSnapshot(S snapshot);

	void clear();
}
