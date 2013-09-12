package org.openlegacy.terminal.support;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionFactory;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.inject.Inject;

public class SimpleTerminalSessionPoolFactory implements TerminalSessionFactory, InitializingBean {

	@Inject
	private AutowireCapableBeanFactory beanFactory;

	private int maxConnections = 2;

	private BlockingQueue<TerminalSession> blockingQueue = new ArrayBlockingQueue<TerminalSession>(maxConnections);

	private List<TerminalSession> actives = new LinkedList<TerminalSession>();

	private Class<TerminalAction> initAction = null;

	private Class<TerminalAction> cleanupAction = null;

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
		ReflectionUtil.newInstance(cleanupAction).perform(terminalSession, null);
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
			TerminalSession terminalSession = new DefaultTerminalSession();
			beanFactory.autowireBean(terminalSession);
			ReflectionUtil.newInstance(initAction).perform(terminalSession, null);
			try {
				blockingQueue.put(terminalSession);
			} catch (InterruptedException e) {
				throw (new OpenLegacyRuntimeException(e));
			}
		}
	}

	public void setInitAction(Class<TerminalAction> initAction) {
		this.initAction = initAction;
	}

	public void setCleanupAction(Class<TerminalAction> cleanupAction) {
		this.cleanupAction = cleanupAction;
	}
}
