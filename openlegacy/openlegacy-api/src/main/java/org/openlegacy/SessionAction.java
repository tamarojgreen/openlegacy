package org.openlegacy;

/**
 * A simple interface for defining a session action
 * 
 */
public interface SessionAction<S extends Session> {

	void perform(S session, Object entity);
}
