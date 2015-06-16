package org.openlegacy;

import org.openlegacy.exceptions.SessionInitException;

public interface SessionPoolListner {

	void newSession() throws SessionInitException;

	void endSession();
}
