/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.providers.tn5250j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.utils.FeatureChecker;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.common.SessionManager;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Properties;

public class Tn5250jTerminalConnectionFactory implements TerminalConnectionFactory, InitializingBean {

	private int waitPauses = 300;
	private int waitTimeout = 30000;

	private Properties properties;
	private Boolean convertToLogical;

	private Properties hostProperties;

	private final static Log logger = LogFactory.getLog(Tn5250jTerminalConnectionFactory.class);
	private static final String DEFAULT_HOST_MODEL = "1";

	private String preferencePath;

	@Override
	public synchronized TerminalConnection getConnection(ConnectionProperties connectionProperties) {

		properties.put(TN5250jConstants.SESSION_HOST, hostProperties.get("host.name"));
		properties.put(TN5250jConstants.SESSION_HOST_PORT, hostProperties.get("host.port"));
		properties.put(TN5250jConstants.SESSION_CODE_PAGE, hostProperties.get("host.codePage"));

		Properties sessionProperties = (Properties)properties.clone();

		sessionProperties.setProperty(TN5250jConstants.SESSION_SCREEN_SIZE, getTn5250JModel());

		setSessionProperties(connectionProperties, sessionProperties);

		Session5250 sessionImpl = SessionManager.instance().openSession(sessionProperties, "", "");

		Tn5250jTerminalConnection olConnection = new Tn5250jTerminalConnection(convertToLogical, waitTimeout);

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
		if (connectionProperties != null) {
			if (connectionProperties.getDeviceName() != null) {
				sessionProperties.put(TN5250jConstants.SESSION_DEVICE_NAME, connectionProperties.getDeviceName());
			}
			if (connectionProperties.getCodePage() != null) {
				sessionProperties.put(TN5250jConstants.SESSION_CODE_PAGE, connectionProperties.getCodePage());
			}
		}
	}

	@Override
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

	public String getPreferencePath() {
		return preferencePath;
	}

	public void setPreferencePath(String preferencePath) {
		this.preferencePath = preferencePath;
	}

	public void afterPropertiesSet() throws Exception {
		if (convertToLogical == null) {
			if (FeatureChecker.isSupportBidi()) {
				logger.info("Found com.ibm.icu library in the classpath. activating convert to logical. To disable define convertToLogical property to false");
				convertToLogical = true;
			}
		}
		Resource resource = null;
		String userFolder = System.getProperty("user.home") + "/.openlegacy";
		// try to find *.properties files in user home directory
		if (!StringUtils.isEmpty(preferencePath)) {
			userFolder = MessageFormat.format("{0}{1}{2}", userFolder, System.getProperty("file.separator"), preferencePath);
		}
		File userDir = new File(userFolder);
		if (userDir.exists()) {
			Collection<File> files = FileUtils.listFiles(userDir, new String[] { "properties" }, false);
			for (File file : files) {
				if (file.getName().equals("host.properties")) {
					resource = new FileSystemResource(file);
				}
			}
		}
		if (resource == null) {
			resource = new ClassPathResource("/host.properties");
		}
		// do not override default properties
		if (hostProperties == null) {
			hostProperties = PropertiesLoaderUtils.loadProperties(resource);
		}

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

	public void setHostProperties(Properties hostProperties) {
		this.hostProperties = hostProperties;
	}
}
