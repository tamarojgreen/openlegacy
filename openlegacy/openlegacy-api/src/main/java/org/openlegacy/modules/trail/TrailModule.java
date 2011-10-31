package org.openlegacy.modules.trail;

import org.openlegacy.modules.HostSessionModule;

/**
 * Session TrailModule module interface
 * 
 */
public interface TrailModule extends HostSessionModule {

	public SessionTrail getSessionTrail();
}
