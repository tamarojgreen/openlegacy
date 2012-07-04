package org.openlegacy.providers.tn5250j;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.tn5250j.Session5250;
import org.tn5250j.framework.common.SessionManager;

import java.util.Properties;

public class Tn5250jTerminalConnectionFactory implements TerminalConnectionFactory {

	private int waitForConnect = 1000;
	private int waitForUnlock = 300;

	private Properties properties;

	public synchronized TerminalConnection getConnection() {

		Session5250 session = SessionManager.instance().openSession(properties, "", "");
		session.connect();

		try {
			Thread.sleep(waitForConnect);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}

		if (!session.isConnected()) {
			throw (new OpenLegacyRuntimeException("Session is not connected"));
		}

		Tn5250jTerminalConnection connection = new Tn5250jTerminalConnection(session);
		connection.setWaitForUnlock(waitForUnlock);
		return connection;
	}

	public void disconnect(TerminalConnection terminalConnection) {
		((Session5250)terminalConnection.getDelegate()).disconnect();
	}

	public void setWaitForConnect(int waitForConnect) {
		this.waitForConnect = waitForConnect;
	}

	public void setWaitForUnlock(int waitForUnlock) {
		this.waitForUnlock = waitForUnlock;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
