package org.openlegacy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.SessionFactory;
import org.openlegacy.SessionPoolListner;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.exceptions.SessionInitException;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public abstract class AbstractSessionPoolFactory<S extends Session, A extends SessionAction<S>> implements SessionFactory<S, A>,
		InitializingBean, DisposableBean {

	private static final Log logger = LogFactory.getLog(AbstractSessionPoolFactory.class);

	private int maxConnections = 2;

	protected BlockingQueue<S> blockingQueue = new ArrayBlockingQueue<S>(maxConnections);

	protected List<S> actives = new LinkedList<S>();

	protected List<S> dirties = new LinkedList<S>();

	protected A initAction = null;

	protected Class<A> cleanupAction = null;

	protected Class<A> keepAliveAction;

	protected long keepAliveInterval = 300000; // 5 minutes by default

	protected long returnSessionsInterval = 100;

	protected Thread keepAliveThread;

	protected boolean stopThreads = false;

	protected Semaphore keepAliveSemaphore = new Semaphore(1, true);

	protected Thread returnSessionsThread;

	private ExecutorService cleanupThreadPool = Executors.newCachedThreadPool();

	public void setStopThreads(boolean stopThreads) {
		this.stopThreads = stopThreads;
	}

	@Autowired(required = false)
	private List<SessionPoolListner> listeners;

	protected void initSession() throws SessionInitException {
		if (listeners != null) {
			for (SessionPoolListner listener : listeners) {
				listener.newSession();
			}
		}
	}

	protected abstract void init();

	@Override
	public S getSession() {
		logger.debug("New session requested");
		logger.debug("blockingQueue: " + blockingQueue.size() + " active sessions: " + actives.size() + " maxConnections: "
				+ maxConnections);
		if (blockingQueue.size() == 0 && actives.size() < maxConnections) {
			try {
				lockKeepAliveSemaphore();
				initSession();
			} catch (SessionInitException e) {
				logger.debug(e.getMessage());
				return null;
			}
		}
		return waitForSession();
	}

	private S waitForSession() {
		try {
			S session = blockingQueue.take();
			actives.add(session);
			unlockKeepAliveSemaphore();
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

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	public void setInitAction(A initAction) {
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

	public void setReturnSessionsInterval(long returnSessionsInterval) {
		this.returnSessionsInterval = returnSessionsInterval;
	}

	@Override
	public void destroy() throws Exception {
		stopThreads = true;
		cleanupThreadPool.shutdown();
	}

	@Override
	public void returnSession(final S session) {
		if (cleanupAction != null) {
			logger.debug(MessageFormat.format("Adding session {0} to dirties for async recycling", session));
			dirties.add(session);
		} else {
			returnSessionInner(session);
		}

	}

	protected void returnSessionInner(final S session) {
		if (session.isConnected()) {
			if (cleanupAction != null) {
				cleanupThreadPool.execute(new Runnable() {

					@Override
					public void run() {
						ReflectionUtil.newInstance(cleanupAction).perform(session, null);
						logger.debug(MessageFormat.format("Session {0} cleanup action {1} performed", session, cleanupAction));
					}
				});
			}
			lockKeepAliveSemaphore();
			blockingQueue.offer(session);
			unlockKeepAliveSemaphore();
			logger.debug(MessageFormat.format("Session {0} offered to queue", session));
		} else {
			endSession();
			logger.debug(MessageFormat.format("Session {0} removed from queue as session is not connected", session));
			lockKeepAliveSemaphore();
			blockingQueue.remove(session);
			unlockKeepAliveSemaphore();
		}
		actives.remove(session);
		logger.debug(MessageFormat.format("Session {0} removed from active sessions", session));
	}

	protected void endSession() {
		if (listeners != null) {
			for (SessionPoolListner listener : listeners) {
				listener.endSession();
			}
		}
	}

	protected void lockKeepAliveSemaphore() {
		try {
			keepAliveSemaphore.acquire(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void unlockKeepAliveSemaphore() {
		try {
			keepAliveSemaphore.release(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
