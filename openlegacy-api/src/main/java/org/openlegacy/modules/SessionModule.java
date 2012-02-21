package org.openlegacy.modules;

/**
 * Define a session plug-able module into Session. Purpose is to allow extending the Session functionality with functionality such
 * as: SessionTrail, LoginModule, Menu, System data, Global data, etc
 * 
 */
public interface SessionModule {

	void destroy();
}
