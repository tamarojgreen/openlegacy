/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.providers.tn5250j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.tn5250j.Session5250;
import org.tn5250j.framework.common.SessionManager;

import java.util.Properties;

public class Tn5250jTerminalConnectionFactory implements TerminalConnectionFactory, InitializingBean {

	private int waitForConnect = 1000;
	private int waitForUnlock = 300;

	private Properties properties;
	private Boolean convertToLogical;

	private final static Log logger = LogFactory.getLog(Tn5250jTerminalConnectionFactory.class);

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

		Tn5250jTerminalConnection connection = new Tn5250jTerminalConnection(session, convertToLogical);
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

	public void setConvertToLogical(boolean convertToLogical) {
		this.convertToLogical = convertToLogical;
	}

	public void afterPropertiesSet() throws Exception {
		if (convertToLogical == null) {
			try {
				Class.forName("com.ibm.icu.text.Bidi");
				convertToLogical = true;
				logger.info("Found com.ibm.icu library in the classpath. activating convert to logical. To disable define convertToLogical property to false");
			} catch (Exception e) {
			}

		}
	}
}
