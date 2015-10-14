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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.exceptions.OpenlegacyRemoteRuntimeException;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.remote.securedgateway.SecuredGatewayUtils;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.ConnectionPropertiesProvider;
import org.openlegacy.terminal.LiveTerminalConnectionFactory;
import org.openlegacy.terminal.MockTerminalConnectionFactory;
import org.openlegacy.terminal.RightAdjust;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.List;

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

	@Override
	public TerminalSnapshot getSnapshot() {
		lazyConnect();

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
				try {
					terminalSnapshot = terminalConnection.fetchSnapshot();
				} catch (RemoteException e) {
					throw (new OpenlegacyRemoteRuntimeException(e));
				}
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

	@Override
	public void doAction(TerminalSendAction terminalSendAction) {
		lazyConnect();
		terminalSnapshot = null;
		handleRightAdjust(terminalSendAction);
		try {
			terminalConnection.doAction(terminalSendAction);
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
		handleSleep(terminalSendAction);
	}

	private static void handleSleep(TerminalSendAction terminalSendAction) {
		if (terminalSendAction.getSleep() > 0) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug(MessageFormat.format("Sleeping for {0} after action {1}", terminalSendAction.getSleep(),
							terminalSendAction.getCommand()));
				}
				Thread.sleep(terminalSendAction.getSleep());
			} catch (InterruptedException e) {
				logger.fatal(e, e);
			}
		}
	}

	private static void handleRightAdjust(TerminalSendAction terminalSendAction) {
		List<TerminalField> fields = terminalSendAction.getFields();
		for (TerminalField terminalField : fields) {
			if (terminalField.getRightAdjust() != RightAdjust.NONE) {
				if (terminalField.getLength() > terminalField.getValue().length()) {
					String fillerChar = terminalField.getRightAdjust() == RightAdjust.ZERO_FILL ? "0" : " ";
					String newValue = null;
					if (terminalField.isRightToLeft()) {
						newValue = StringUtils.rightPad(terminalField.getValue(), terminalField.getLength(), fillerChar);
					} else {
						newValue = StringUtils.leftPad(terminalField.getValue(), terminalField.getLength(), fillerChar);
					}
					terminalField.setValue(newValue);
				}
			}
		}
	}

	@Override
	public Object getDelegate() {
		lazyConnect();
		try {
			return terminalConnection.getDelegate();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	private void lazyConnect() {
		if (terminalConnection == null) {
			TerminalConnectionFactory terminalConnectionFactory = getConnectionFactory();
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

	private TerminalConnectionFactory getConnectionFactory() {
		final OpenLegacyProperties olProperties = applicationContext.getBean(OpenLegacyProperties.class);
		boolean isLiveSession = olProperties.isLiveSession();
		TerminalConnectionFactory terminalConnectionFactory;
		if (isLiveSession) {
			terminalConnectionFactory = (TerminalConnectionFactory)SecuredGatewayUtils.getProxyBeanByType(
					LiveTerminalConnectionFactory.class, applicationContext);
		} else {
			terminalConnectionFactory = (TerminalConnectionFactory)SecuredGatewayUtils.getProxyBeanByType(
					MockTerminalConnectionFactory.class, applicationContext);
		}
		return terminalConnectionFactory;
	}

	@Override
	public void disconnect() {
		logger.info("Disconnecting session");

		if (terminalConnection == null) {
			logger.debug("Session not connected");
			return;
		}
		TerminalConnectionFactory terminalConnectionFactory = getConnectionFactory();
		try {
			terminalConnectionFactory.disconnect(terminalConnection);
		} catch (Exception e) {
			logger.warn("Error disconnecting session", e);
		}
		terminalConnection = null;
		terminalSnapshot = null;
	}

	@Override
	public boolean isConnected() {
		try {
			return terminalConnection != null && terminalConnection.isConnected();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	@Override
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

	@Override
	public Integer getSequence() {
		if (!isConnected()) {
			return 0;
		}
		try {
			return terminalConnection.getSequence();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	public Object readResolve() {
		this.applicationContext = SpringUtil.getApplicationContext();
		return this;
	}

	@Override
	public void flip() {
		try {
			terminalConnection.flip();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}

	@Override
	public boolean isRightToLeftState() {
		if (terminalConnection == null) {
			return false;
		}
		try {
			return terminalConnection.isRightToLeftState();
		} catch (RemoteException e) {
			throw (new OpenlegacyRemoteRuntimeException(e));
		}
	}
}
