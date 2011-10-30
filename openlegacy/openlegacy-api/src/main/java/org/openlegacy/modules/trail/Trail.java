package org.openlegacy.modules.trail;

import org.openlegacy.modules.HostSessionModule;

/**
 * Session Trail module interface. Uses of the name "Trail" is for shorter API accessibility
 * 
 */
public interface Trail extends HostSessionModule {

	public SessionTrail getSessionTrail();
}
