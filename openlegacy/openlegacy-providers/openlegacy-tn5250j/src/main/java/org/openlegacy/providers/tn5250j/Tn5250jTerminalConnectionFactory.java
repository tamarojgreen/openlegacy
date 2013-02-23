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
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.common.SessionManager;

import java.util.Properties;

public class Tn5250jTerminalConnectionFactory implements TerminalConnectionFactory, InitializingBean {

	private int waitPauses = 300;
	private int waitTimeout = 30000;

	private Properties properties;
	private Boolean convertToLogical;

	private Properties hostProperties;

	private final static Log logger = LogFactory.getLog(Tn5250jTerminalConnectionFactory.class);
	private static final String DEFAULT_HOST_MODEL = "1";

	public synchronized TerminalConnection getConnection(ConnectionProperties connectionProperties) {

		Properties sessionProperties = (Properties)properties.clone();

		sessionProperties.setProperty(TN5250jConstants.SESSION_SCREEN_SIZE, getTn5250JModel());

		setSessionProperties(connectionProperties, sessionProperties);

		Session5250 sessionImpl = SessionManager.instance().openSession(sessionProperties, "", "");

		Tn5250jTerminalConnection olConnection = new Tn5250jTerminalConnection(convertToLogical);

		sessionImpl.connect();

		olConnection.setSession(sessionImpl);

		sessionImpl.addSessionListener(olConnection);

		int totalWait = 0;
		while (olConnection.getSequence() == 0 && totalWait < waitTimeout) {
			try {
				Thread.sleep(waitPauses);
				totalWait += waitPauses;
			} catch (InterruptedException e) {
				throw (new OpenLegacyRuntimeException("Session is not connected"));
			}
		}

		if (!olConnection.isConnected()) {
			throw (new OpenLegacyRuntimeException("Session is not connected"));
		}

		sessionImpl.getScreen().addScreenListener(olConnection);
		olConnection.setWaitForUnlock(waitPauses);
		return olConnection;
	}

	private static void setSessionProperties(ConnectionProperties connectionProperties, Properties sessionProperties) {
		if (connectionProperties != null && connectionProperties.getDeviceName() != null) {
			sessionProperties.put(TN5250jConstants.SESSION_DEVICE_NAME, connectionProperties.getDeviceName());
		}
	}

	public void disconnect(TerminalConnection terminalConnection) {
		((Session5250)terminalConnection.getDelegate()).disconnect();
	}

	public void setWaitPauses(int waitPauses) {
		this.waitPauses = waitPauses;
	}

	public void setWaitTimeout(int waitTimeout) {
		this.waitTimeout = waitTimeout;
	}

	public Properties getProperties() {
		return properties;
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
		Resource resource = new ClassPathResource("/host.properties");
		hostProperties = PropertiesLoaderUtils.loadProperties(resource);

	}

	private String getTn5250JModel() {
		String property = hostProperties.getProperty("host.model");
		if (property != null) {
			if (property.equals("2")) {
				return "0";
			}
			return "1";
		}
		return DEFAULT_HOST_MODEL;
	}
}
