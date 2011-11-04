package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;
import org.openlegacy.modules.HostSessionModule;

/**
 * Session TrailModule module interface
 * 
 */
public interface Trail extends HostSessionModule {

	public SessionTrail<? extends Snapshot> getSessionTrail();
}
