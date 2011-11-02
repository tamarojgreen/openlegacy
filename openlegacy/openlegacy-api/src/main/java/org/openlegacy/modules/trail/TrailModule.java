package org.openlegacy.modules.trail;

import org.openlegacy.modules.HostSessionModule;
import org.openlegacy.terminal.TerminalSnapshot;

/**
 * Session TrailModule module interface
 * 
 */
public interface TrailModule extends HostSessionModule {

	public SessionTrail<TerminalSnapshot> getSessionTrail();
}
