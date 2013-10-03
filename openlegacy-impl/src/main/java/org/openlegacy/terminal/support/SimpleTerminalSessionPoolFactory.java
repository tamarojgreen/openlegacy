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
		try {
			TerminalSession terminalSession = blockingQueue.take();
			actives.add(terminalSession);
			return terminalSession;
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	public void returnSession(TerminalSession terminalSession) {
		if (cleanupAction != null) {
			ReflectionUtil.newInstance(cleanupAction).perform(terminalSession, null);
		}
		actives.remove(terminalSession);
		blockingQueue.offer(terminalSession);
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
		for (int i = 0; i < maxConnections; i++) {
			TerminalSession terminalSession = applicationContext.getBean(TerminalSession.class);
			if (initAction != null) {
				ReflectionUtil.newInstance(initAction).perform(terminalSession, null);
			}
			try {
				blockingQueue.put(terminalSession);
			} catch (InterruptedException e) {
				throw (new OpenLegacyRuntimeException(e));
			}
		}

		if (keepAliveAction != null) {
			keepAliveThread = new Thread() {

				@Override
				public void run() {
					while (!stopKeepAlive) {
						try {
							sleep(keepAliveInterval);
						} catch (InterruptedException e) {
							throw (new RuntimeException(e));
						}
						TerminalAction keepAliveAction1 = ReflectionUtil.newInstance(keepAliveAction);
						TerminalSession[] sessions = blockingQueue.toArray(new TerminalSession[blockingQueue.size()]);
						for (TerminalSession session : sessions) {
							if (!actives.contains(session)) {
								try {
									session.doAction(keepAliveAction1);
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
