package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.inject.Inject;

public class SimpleTerminalSessionPoolFactory implements TerminalSessionFactory, InitializingBean, DisposableBean {

	private static final Log logger = LogFactory.getLog(SimpleTerminalSessionPoolFactory.class);

	@Inject
	private ApplicationContext applicationContext;

	private int maxConnections = 2;

	private BlockingQueue<TerminalSession> blockingQueue = new ArrayBlockingQueue<TerminalSession>(maxConnections);

	private List<TerminalSession> actives = new LinkedList<TerminalSession>();

	private Class<TerminalAction> initAction = null;

	private Class<TerminalAction> cleanupAction = null;

	private Class<TerminalAction> keepAliveAction;

	private long keepAliveInterval = 300000; // 5 minutes by default

	private Thread keepAliveThread;

	private boolean stopKeepAlive = false;

	public TerminalSession getSession() {
		logger.debug("New session requested");
		if (actives.size() < maxConnections) {
			initSession();
		}
		return waitForSession();
	}

	private TerminalSession waitForSession() {
		try {
			TerminalSession terminalSession = blockingQueue.take();
			actives.add(terminalSession);
			logger.debug(MessageFormat.format("Session {0} pulled from blocking queue, and added to active sessions",
					terminalSession));
			return terminalSession;
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	public void returnSession(final TerminalSession terminalSession) {
		if (terminalSession.isConnected()) {
			if (cleanupAction != null) {
				ReflectionUtil.newInstance(cleanupAction).perform(terminalSession, null);
				logger.debug(MessageFormat.format("Session {0} cleanup action {1} performed", terminalSession, cleanupAction));
			}
			blockingQueue.offer(terminalSession);
			logger.debug(MessageFormat.format("Session {0} offered to queue", terminalSession));
		} else {
			logger.debug(MessageFormat.format("Session {0} removed from queue as session is not connected", terminalSession));
			blockingQueue.remove(terminalSession);
		}
		actives.remove(terminalSession);
		logger.debug(MessageFormat.format("Session {0} removed from active sessions", terminalSession));

	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public List<TerminalSession> getActives() {
		return actives;
	}

	public void afterPropertiesSet() throws Exception {
		init();
	}

	private void init() {

		if (keepAliveAction != null) {
			keepAliveThread = new Thread() {

				@Override
				public void run() {
					while (!stopKeepAlive) {
						try {
							logger.debug("Keep alive is sleeping");
							sleep(keepAliveInterval);
						} catch (InterruptedException e) {
							throw (new RuntimeException(e));
						}
						TerminalAction keepAliveAction1 = ReflectionUtil.newInstance(keepAliveAction);
						TerminalSession[] sessions = blockingQueue.toArray(new TerminalSession[blockingQueue.size()]);
						logger.debug(sessions.length + " sessions found in queue during keep alive");
						for (TerminalSession session : sessions) {
							if (!actives.contains(session)) {
								try {
									session.doAction(keepAliveAction1);
									logger.debug("Keep alive action " + keepAliveAction.getSimpleName() + " performed for "
											+ session);
								} catch (Exception e) {
									logger.fatal(e);
								}
							}
						}
					}
				}
			};
			keepAliveThread.start();
		}
	}

	private void initSession() {
		TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
		logger.debug(MessageFormat.format("New session {0} created for pool", terminalSession));
		if (initAction != null) {
			ReflectionUtil.newInstance(initAction).perform(terminalSession, null);
			logger.debug(MessageFormat.format("New session {0} init action {1} performed", terminalSession,
					initAction.getSimpleName()));
		}
		try {
			blockingQueue.put(terminalSession);
			logger.debug("New session " + terminalSession + " added to blocking queue");
		} catch (InterruptedException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	public void setInitAction(Class<TerminalAction> initAction) {
		this.initAction = initAction;
	}

	public void setCleanupAction(Class<TerminalAction> cleanupAction) {
		this.cleanupAction = cleanupAction;
	}

	public void setKeepAliveAction(Class<TerminalAction> keepAliveAction) {
		this.keepAliveAction = keepAliveAction;
	}

	public Class<TerminalAction> getKeepAliveAction() {
		return keepAliveAction;
	}

	public void setKeepAliveInterval(long keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
	}

	public void destroy() throws Exception {
		stopKeepAlive = true;
	}
}
