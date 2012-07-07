package org.openlegacy.modules;

/**
 * Define a plug-able module into a session. Purpose is to allow extending the session functionality with functionality such as:
 * {@link org.openlegacy.modules.trail.Trail}, {@link org.openlegacy.modules.login.Login},
 * {@link org.openlegacy.modules.menu.Menu}, etc <br/>
 * Example: <code>terminalSession.getModule(Login.class).login("user","password")</code>
 * 
 * @author Roi Mor
 * 
 */
public interface SessionModule {

	void destroy();
}
