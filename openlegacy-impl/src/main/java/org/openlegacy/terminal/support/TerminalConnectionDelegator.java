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
package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.ConnectionPropertiesProvider;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.inject.Inject;

public class TerminalConnectionDelegator implements TerminalConnection, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	private TerminalConnection terminalConnection;

	private TerminalSnapshot terminalSnapshot;

	private int waitBetweenEmptyScreens = 50;
	private int maxWaitOnEmptyScreen = 10000;

	private final static Log logger = LogFactory.getLog(TerminalConnectionDelegator.class);

	public TerminalSnapshot getSnapshot() {
		lazyConnect();

		// clear the snapshot sequence is different from the session, clear it so it will re-build
		if (terminalSnapshot != null && terminalSnapshot.getSequence() != null
				&& !terminalConnection.getSequence().equals(terminalSnapshot.getSequence())) {
			terminalSnapshot = null;
		}

		waitForNonEmptySnapshot();

		if (!isConnected()) {
			disconnect();
			throw (new OpenLegacyRuntimeException("Session is not connected"));
		}

		if (terminalSnapshot == null) {
			throw (new OpenLegacyProviderException(MessageFormat.format(
					"Current screen is empty for session after waiting for {0}", maxWaitOnEmptyScreen)));
		}
		return terminalSnapshot;
	}

	private int waitForNonEmptySnapshot() {
		int timer = 0;
		do {
			if (terminalSnapshot == null) {
				terminalSnapshot = terminalConnection.fetchSnapshot();
			}

			if (terminalSnapshot.getFields().size() == 0) {
				try {
					Thread.sleep(waitBetweenEmptyScreens);
					timer += waitBetweenEmptyScreens;
				} catch (InterruptedException e) {
					// do nothing
				}
				terminalSnapshot = null;
			}

		} while (isConnected() && terminalSnapshot == null && timer < maxWaitOnEmptyScreen);
		return timer;
	}

	public void doAction(TerminalSendAction terminalSendAction) {
		lazyConnect();
		terminalSnapshot = null;
		terminalConnection.doAction(terminalSendAction);
	}

	public Object getDelegate() {
		lazyConnect();
		return terminalConnection.getDelegate();
	}

	private void lazyConnect() {
		if (terminalConnection == null) {
			TerminalConnectionFactory terminalConnectionFactory = applicationContext.getBean(TerminalConnectionFactory.class);
			ConnectionPropertiesProvider connectionPropertiesProvider = applicationContext.getBean(ConnectionPropertiesProvider.class);
			ConnectionProperties connectionProperties = connectionPropertiesProvider.getConnectionProperties();
			terminalConnection = terminalConnectionFactory.getConnection(connectionProperties);
			logger.info("Opened new session");
		}
		if (!isConnected()) {
			terminalConnection = null;
			throw (new SessionEndedException("Session is not connected"));
		}
	}

	public void disconnect() {
		logger.info("Disconnecting session");

		if (terminalConnection == null) {
			logger.debug("Session not connected");
			return;
		}
		TerminalConnectionFactory terminalConnectionFactory = applicationContext.getBean(TerminalConnectionFactory.class);
		try {
			terminalConnectionFactory.disconnect(terminalConnection);
		} catch (Exception e) {
			logger.warn("Error disconnecting session", e);
		}
		terminalConnection = null;
		terminalSnapshot = null;
	}

	public boolean isConnected() {
		return terminalConnection != null && terminalConnection.isConnected();
	}

	public TerminalSnapshot fetchSnapshot() {
		terminalSnapshot = null;
		return getSnapshot();
	}

	public void setWaitBetweenEmptyScreens(int waitBetweenEmptyScreens) {
		this.waitBetweenEmptyScreens = waitBetweenEmptyScreens;
	}

	public void setMaxWaitOnEmptyScreen(int maxWaitOnEmptyScreen) {
		this.maxWaitOnEmptyScreen = maxWaitOnEmptyScreen;
	}

	public Integer getSequence() {
		if (!isConnected()) {
			return 0;
		}
		return terminalConnection.getSequence();
	}

	public Object readResolve() {
		this.applicationContext = SpringUtil.getApplicationContext();
		return this;
	}

	public void flip() {
		terminalConnection.flip();
	}

	public boolean isRightToLeftState() {
		return terminalConnection.isRightToLeftState();
	}
}
