package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.SessionModule;

/**
 * Session TrailModule module interface
 * 
 */
public interface Trail extends SessionModule {

	public SessionTrail<? extends Snapshot> getSessionTrail();
}
