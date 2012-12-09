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
package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.SessionProperties;
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.support.AbstractEntitiesRegistry;
import org.openlegacy.support.AbstractSession;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.ConnectionPropertiesProvider;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionListener;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSessionPropertiesConsts;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.services.ScreensRecognizer;
import org.openlegacy.terminal.services.SessionNavigator;
import org.openlegacy.terminal.support.proxy.ScreenEntityMethodInterceptor;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.terminal.wait_conditions.WaitCondition;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * A default session class exposes screenEntity building and sending
 * 
 * 
 */
public class DefaultTerminalSession extends AbstractSession implements TerminalSession, Serializable, ConnectionPropertiesProvider {

	private static final long serialVersionUID = 1L;

	@Inject
	private List<ScreenEntityBinder> screenEntityBinders;

	private TerminalConnection terminalConnection;

	private ScreenEntity entity;

	@Inject
	private SessionNavigator sessionNavigator;

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private TerminalActionMapper terminalActionMapper;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	@Inject
	private SessionPropertiesProvider sessionPropertiesProvider;

	private ScreenEntityMethodInterceptor interceptor;

	private final static Log logger = LogFactory.getLog(DefaultTerminalSession.class);

	private boolean useProxyForEntities = true;

	private ConnectionProperties connectionProperties;

	private SessionProperties sessionProperties;

	@SuppressWarnings("unchecked")
	public <S> S getEntity(Class<S> screenEntityClass, Object... keys) throws EntityNotFoundException {

		checkRegistryDirty();

		if (entity == null) {
			entity = getEntityInner();
		}
		if (!screenEntityUtils.isEntitiesEquals(entity, screenEntityClass, keys)) {
			resetEntity();
		}
		if (entity == null) {
			sessionNavigator.navigate(this, screenEntityClass, keys);
			entity = getEntityInner();
		}
		return (S)entity;
	}

	private void checkRegistryDirty() {
		if (screenEntitiesRegistry.isDirty()) {
			resetEntity();
			// set the registry back to clean - for design-time purposes only!
			((AbstractEntitiesRegistry<?, ?>)screenEntitiesRegistry).setDirty(false);
		}
	}

	private <S> ScreenEntity getEntityInner() {
		resetEntity();
		TerminalSnapshot terminalSnapshot = getSnapshot();

		Class<?> matchedScreenEntity = screensRecognizer.match(terminalSnapshot);
		if (matchedScreenEntity == null) {
			return null;
		}

		ScreenEntity screenEntity = null;
		if (useProxyForEntities) {
			screenEntity = (ScreenEntity)ProxyUtil.createPojoProxy(matchedScreenEntity, ScreenEntity.class, interceptor);
		} else {
			screenEntity = (ScreenEntity)ReflectionUtil.newInstance(matchedScreenEntity);
		}

		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(matchedScreenEntity);
		if (screenEntityDefinition.isPerformDefaultBinding()) {
			for (ScreenEntityBinder screenEntityBinder : screenEntityBinders) {
				screenEntityBinder.populateEntity(screenEntity, terminalSnapshot);
			}
		}

		List<ScreenEntityBinder> binders = screenEntityDefinition.getBinders();
		if (binders != null) {
			for (ScreenEntityBinder screenEntityBinder : binders) {
				screenEntityBinder.populateEntity(screenEntity, terminalSnapshot);
			}
		}

		return screenEntity;
	}

	private void resetEntity() {
		if (entity != null) {
			entity = null;
		}
	}

	@SuppressWarnings("unchecked")
	public <R extends ScreenEntity> R getEntity() {
		checkRegistryDirty();
		if (entity == null) {
			entity = getEntityInner();
		}
		return (R)entity;
	}

	@SuppressWarnings("unchecked")
	public <R extends ScreenEntity> R doAction(TerminalAction action, WaitCondition... waitConditions) {
		return (R)doAction(action, null, waitConditions);
	}

	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction terminalAction, S screenEntity,
			Class<R> expectedEntity) {
		try {
			@SuppressWarnings("unchecked")
			R object = (R)doAction(terminalAction, screenEntity);
			return object;
		} catch (ClassCastException e) {
			throw (new ScreenEntityNotAccessibleException(e));
		}

	}

	@SuppressWarnings("unchecked")
	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction terminalAction, S screenEntity,
			WaitCondition... waitConditions) {

		// verify screens are synch
		if (screenEntity != null) {
			getEntity(screenEntity.getClass());
		}

		resetEntity();
		Object command = terminalActionMapper.getCommand(terminalAction);
		if (command == null) {
			terminalAction.perform(this, screenEntity);
		} else {
			SimpleTerminalSendAction sendAction = new SimpleTerminalSendAction(command);

			if (screenEntity != null) {
				ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity.getClass());
				if (screenEntityDefinition.isPerformDefaultBinding()) {
					for (ScreenEntityBinder screenEntityBinder : screenEntityBinders) {
						screenEntityBinder.populateSendAction(sendAction, getSnapshot(), screenEntity);
					}
				}
				List<ScreenEntityBinder> binders = screenEntityDefinition.getBinders();
				if (binders != null) {
					for (ScreenEntityBinder screenEntityBinder : binders) {
						screenEntityBinder.populateSendAction(sendAction, getSnapshot(), screenEntity);

					}
				}
			}

			doAction(sendAction, waitConditions);
		}

		return (R)getEntity();
	}

	private void notifyModulesBeforeConnect() {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).beforeConnect(terminalConnection);
			}
		}
	}

	private void notifyModulesAfterConnect() {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).afterConnect(terminalConnection);
			}
		}
	}

	protected void notifyModulesBeforeSend(TerminalSendAction terminalSendAction) {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).beforeSendAction(terminalConnection, terminalSendAction);
			}
		}
	}

	protected void notifyModulesAfterSend() {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).afterSendAction(terminalConnection);
			}
		}
	}

	public TerminalSnapshot getSnapshot() {
		boolean newSession = false;
		if (!terminalConnection.isConnected()) {
			notifyModulesBeforeConnect();
			newSession = true;
		}
		TerminalSnapshot snapshot = terminalConnection.getSnapshot();

		if (newSession) {
			notifyModulesAfterConnect();
		}
		return snapshot;
	}

	public Object getDelegate() {
		return terminalConnection.getDelegate();
	}

	public void setTerminalConnection(TerminalConnection terminalConnection) {
		this.terminalConnection = terminalConnection;
	}

	public void disconnect() {
		List<? extends SessionModule> sessionModulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : sessionModulesList) {
			sessionModule.destroy();
		}
		resetEntity();
		terminalConnection.disconnect();
	}

	public boolean isConnected() {
		return terminalConnection.isConnected();
	}

	public void setInterceptor(ScreenEntityMethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		interceptor.setTerminalSession(this);
	}

	public void doAction(TerminalSendAction sendAction, WaitCondition... waitConditions) {
		logScreenBefore(sendAction);
		// sort the modified fields by position
		Collections.sort(sendAction.getModifiedFields(), TerminalPositionContainerComparator.instance());

		notifyModulesBeforeSend(sendAction);
		doTerminalAction(sendAction);
		performWait(waitConditions);
		notifyModulesAfterSend();

		logScreenAfter();
	}

	private void performWait(WaitCondition... waitConditions) {
		if (waitConditions == null || waitConditions.length == 0) {
			return;
		}

		int totalWait = 0;

		for (WaitCondition waitCondition : waitConditions) {
			fetchSnapshot();
			resetEntity();
			while (waitCondition.continueWait(this) && totalWait < waitCondition.getWaitTimeout()) {
				if (logger.isTraceEnabled()) {
					logger.trace(MessageFormat.format("Waiting for {0}ms. Current screen is:{1}",
							waitCondition.getWaitInterval(), fetchSnapshot()));
				}
				try {
					Thread.sleep(waitCondition.getWaitInterval());
				} catch (InterruptedException e) {
					throw (new OpenLegacyRuntimeException(e));
				}

				totalWait += waitCondition.getWaitInterval();
				fetchSnapshot();
				resetEntity();
			}
		}
	}

	protected void doTerminalAction(TerminalSendAction sendAction, WaitCondition... waitConditions) {
		terminalConnection.doAction(sendAction);
		resetEntity();
	}

	private void logScreenBefore(TerminalSendAction sendAction) {
		if (logger.isDebugEnabled()) {
			logger.debug(MessageFormat.format("\nAction:{0}, Cursor:{1}\n", sendAction.getCommand(),
					sendAction.getCursorPosition()));
			logger.debug("\nScreen before\n(* abc * marks a modified field, [ abc ] mark an input field, # mark cursor):\n\n"
					+ getSnapshot());
		}
	}

	protected void logScreenAfter() {
		if (logger.isDebugEnabled()) {
			logger.debug("\n\nScreen after ([ abc ] indicates a input field):\n\n" + getSnapshot());
		}
	}

	public TerminalSnapshot fetchSnapshot() {
		return terminalConnection.fetchSnapshot();
	}

	protected TerminalConnection getTerminalConnection() {
		return terminalConnection;
	}

	protected ScreensRecognizer getScreensRecognizer() {
		return screensRecognizer;
	}

	public Object getEntity(String entityName, Object... keys) throws EntityNotFoundException {
		Class<?> entityClass = screenEntitiesRegistry.getEntityClass(entityName);
		if (entityClass == null) {
			throw (new EntityNotFoundException(MessageFormat.format("Screen entity \"{0}\" not found", entityName)));
		}
		return getEntity(entityClass, keys);
	}

	protected ScreenEntitiesRegistry getScreenEntitiesRegistry() {
		return screenEntitiesRegistry;
	}

	public Integer getSequence() {
		return terminalConnection.getSequence();
	}

	public <R extends ScreenEntity> void setEntity(R entity) {
		this.entity = entity;
	}

	public boolean isUseProxyForEntities() {
		return useProxyForEntities;
	}

	public void setUseProxyForEntities(boolean useProxyForEntities) {
		this.useProxyForEntities = useProxyForEntities;
	}

	public void setSessionPropertiesProvider(SessionPropertiesProvider sessionPropertiesProvider) {
		this.sessionPropertiesProvider = sessionPropertiesProvider;
	}

	public SessionProperties getProperties() {
		if (sessionProperties == null) {
			sessionProperties = sessionPropertiesProvider.getSessionProperties();
		}
		return sessionProperties;
	}

	public ConnectionProperties getConnectionProperties() {
		if (connectionProperties == null) {
			connectionProperties = new ConnectionProperties() {

				public String getDeviceName() {
					return (String)getProperties().getProperty(TerminalSessionPropertiesConsts.DEVICE_NAME);
				}

			};
		}
		return connectionProperties;
	}

}
