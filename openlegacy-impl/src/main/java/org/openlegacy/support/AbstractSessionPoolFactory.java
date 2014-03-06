package org.openlegacy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.SessionFactory;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractSessionPoolFactory<S extends Session, A extends SessionAction<S>> implements SessionFactory<S, A>, InitializingBean, DisposableBean {

	private static final Log logger = LogFactory.getLog(AbstractSessionPoolFactory.class);

	private int maxConnections = 2;

	protected BlockingQueue<S> blockingQueue = new ArrayBlockingQueue<S>(maxConnections);

	protected List<S> actives = new LinkedList<S>();

	protected Class<A> initAction = null;

	protected Class<A> cleanupAction = null;

	protected Class<A> keepAliveAction;

	protected long keepAliveInterval = 300000; // 5 minutes by default

	protected Thread keepAliveThread;

	protected boolean stopKeepAlive = false;

	protected abstract void initSession();

	protected abstract void init();

	public S getSession() {
		logger.debug("New session requested");
		if (blockingQueue.size() == 0 && actives.size() < maxConnections) {
			initSession();
		}
		return waitForSession();
	}

	private S waitForSession() {
		try {
			S session = blockingQueue.take();
			actives.add(session);
			logger.debug(MessageFormat.format("Session {0} pulled from blocking queue, and added to active sessions", session));
			return session;
		} catch (Exception e) {
			throw (new OpenLegacyRuntimeException(e));
		}

	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public List<S> getActives() {
		return actives;
	}

	public void afterPropertiesSet() throws Exception {
		init();
	}

	public void setInitAction(Class<A> initAction) {
		this.initAction = initAction;
	}

	public void setCleanupAction(Class<A> cleanupAction) {
		this.cleanupAction = cleanupAction;
	}

	public void setKeepAliveAction(Class<A> keepAliveAction) {
		this.keepAliveAction = keepAliveAction;
	}

	public Class<A> getKeepAliveAction() {
		return keepAliveAction;
	}

	public void setKeepAliveInterval(long keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
	}

	public void destroy() throws Exception {
		stopKeepAlive = true;
	}

	public void returnSession(final S session) {
		if (session.isConnected()) {
			if (cleanupAction != null) {
				ReflectionUtil.newInstance(cleanupAction).perform(session, null);
				logger.debug(MessageFormat.format("Session {0} cleanup action {1} performed", session, cleanupAction));
			}
			blockingQueue.offer(session);
			logger.debug(MessageFormat.format("Session {0} offered to queue", session));
		} else {
			logger.debug(MessageFormat.format("Session {0} removed from queue as session is not connected", session));
			blockingQueue.remove(session);
		}
		actives.remove(session);
		logger.debug(MessageFormat.format("Session {0} removed from active sessions", session));

	}

}
