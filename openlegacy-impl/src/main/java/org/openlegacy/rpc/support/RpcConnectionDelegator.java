package org.openlegacy.rpc.support;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.exceptions.SessionEndedException;
import org.openlegacy.rpc.LiveHostRpcConnectionFactory;
import org.openlegacy.rpc.MockHostRpcConnectionFactory;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcConnectionFactory;
import org.openlegacy.terminal.support.TerminalConnectionDelegator;
import org.springframework.context.ApplicationContext;

public class RpcConnectionDelegator implements RpcConnectionFactory, Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	private RpcConnection terminalConnection;

	private final static Log logger = LogFactory.getLog(TerminalConnectionDelegator.class);

	public RpcConnection getConnection() {
		if (terminalConnection == null) {
			final OpenLegacyProperties olProperties = applicationContext.getBean(OpenLegacyProperties.class);
			boolean isLiveSession = olProperties.isLiveSession();
			RpcConnectionFactory terminalConnectionFactory;
			if (isLiveSession){
				terminalConnectionFactory = applicationContext.getBean(LiveHostRpcConnectionFactory.class);
			}else{
				terminalConnectionFactory = applicationContext.getBean(MockHostRpcConnectionFactory.class);
			}
			terminalConnection = terminalConnectionFactory.getConnection();
			logger.info("Opened new session");
		}
		if (!isConnected()) {
			terminalConnection = null;
			throw (new SessionEndedException("Session is not connected"));
		}
		
		return terminalConnection;
	}

	public boolean isConnected() {
		return terminalConnection != null && terminalConnection.isConnected();
	}
	
	@Override
	public void disconnect(RpcConnection rpcConnection) {
		logger.info("Disconnecting session");

		if (terminalConnection == null) {
			logger.debug("Session not connected");
			return;
		}
		RpcConnectionFactory terminalConnectionFactory = applicationContext.getBean(RpcConnectionFactory.class);
		try {
			terminalConnectionFactory.disconnect(terminalConnection);
		} catch (Exception e) {
			logger.warn("Error disconnecting session", e);
		}
		terminalConnection = null;
	}

}
