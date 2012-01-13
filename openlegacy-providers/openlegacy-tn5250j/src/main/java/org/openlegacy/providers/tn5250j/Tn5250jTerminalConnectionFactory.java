package org.openlegacy.providers.tn5250j;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.tn5250j.Session5250;
import org.tn5250j.beans.ProtocolBean;
import org.tn5250j.framework.common.SessionManager;

public class Tn5250jTerminalConnectionFactory implements TerminalConnectionFactory {

	private ProtocolBean protocolBean;

	private int waitForConnect = 1000;
	private int waitForUnlock = 300;

	public synchronized TerminalConnection getConnection() {

		Session5250 session = protocolBean.getSession();

		protocolBean.connect();

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

	public void setProtocolBean(ProtocolBean protocolBean) {
		this.protocolBean = protocolBean;
	}

	public void setWaitForConnect(int waitForConnect) {
		this.waitForConnect = waitForConnect;
	}

	public void setWaitForUnlock(int waitForUnlock) {
		this.waitForUnlock = waitForUnlock;
	}

	public ProtocolBean getProtocolBean() {
		int count = SessionManager.instance().getSessions().getCount();
		return new ProtocolBean("", "Session" + String.valueOf(count));
	}
}
