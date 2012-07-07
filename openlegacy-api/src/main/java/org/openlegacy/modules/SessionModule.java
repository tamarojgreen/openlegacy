package org.openlegacy.modules;

import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.trail.Trail;

/**
 * Define a plug-able module into a session. Purpose is to allow extending the session functionality with functionality such as:
 * {@link Trail}, {@link Login}, {@link Menu}, etc <br/>
 * Example: <code>terminalSession.getModule(Login.class).login("user","password")</code>
 * 
 */
public interface SessionModule {

	void destroy();
}
