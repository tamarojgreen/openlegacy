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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.ApplicationConnectionListener;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.exceptions.EntityNotAccessibleException;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.support.AbstractSession;
import org.openlegacy.terminal.ConnectionProperties;
import org.openlegacy.terminal.ConnectionPropertiesProvider;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalField;
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
import org.openlegacy.terminal.wait_conditions.WaitCondition;
import org.openlegacy.utils.EntityUtils;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

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
	private EntityUtils entityUtils;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Inject
	private transient ApplicationContext applicationContext;

	private ScreenEntityMethodInterceptor interceptor;

	private final static Log logger = LogFactory.getLog(DefaultTerminalSession.class);

	private boolean useProxyForEntities = true;

	private ConnectionProperties connectionProperties;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@SuppressWarnings("unchecked")
	public <S> S getEntity(Class<S> screenEntityClass, Object... keys) throws EntityNotFoundException {

		if (keys.length == 1 && keys[0] == null) {
			keys = new Object[0];
		}

		checkRegistryDirty();

		if (entity == null) {
			entity = getEntityInner();
		}
		if (!entityUtils.isEntitiesEquals(screenEntitiesRegistry, entity, screenEntityClass, keys)) {
			resetEntity();
		}
		ScreenEntityDefinition definitions = getScreenEntitiesRegistry().get(screenEntityClass);
		if (keys.length > definitions.getKeys().size()) {
			throw (new EntityNotAccessibleException(
					MessageFormat.format(
							"Requested entity {0} with keys {1} doesnt matches the defined entity keys count: {2}. Verify key is defined for {3}",
							screenEntityClass, ArrayUtils.toString(keys), definitions.getKeys().size(),
							screenEntityClass.getName())));
		}
		if (entity == null) {
			sessionNavigator.navigate(this, screenEntityClass, keys);
			entity = getEntityInner();
		}
		return (S)entity;
	}

	private void checkRegistryDirty() {
		if (getScreenEntitiesRegistry().isDirty()) {
			resetEntity();
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

		ScreenEntityDefinition screenEntityDefinition = getScreenEntitiesRegistry().get(matchedScreenEntity);
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
				ScreenEntityDefinition screenEntityDefinition = getScreenEntitiesRegistry().get(screenEntity.getClass());
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

	protected void notifyModulesBeforeSend(TerminalSendAction terminalSendAction) {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof ApplicationConnectionListener) {
				((ApplicationConnectionListener)sessionModule).beforeAction(terminalConnection, terminalSendAction);
			}
		}
	}

	protected void notifyModulesAfterAction(TerminalSendAction sendAction) {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof ApplicationConnectionListener) {
				((ApplicationConnectionListener)sessionModule).afterAction(terminalConnection, sendAction, getSnapshot());
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

	public void setConnection(TerminalConnection terminalConnection) {
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

		doTerminalAction(sendAction, waitConditions);
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

		formatSendAction(sendAction);

		logScreenBefore(sendAction);

		notifyModulesBeforeSend(sendAction);

		terminalConnection.doAction(sendAction);
		resetEntity();

		performWait(waitConditions);

		logScreenAfter();

		notifyModulesAfterAction(sendAction);
	}

	protected void formatSendAction(TerminalSendAction sendAction) {
		List<TerminalField> fields = sendAction.getModifiedFields();
		for (TerminalField terminalField : fields) {
			if (openLegacyProperties.isUppercaseInput() || terminalField.isUppercase()) {
				terminalField.setValue(terminalField.getValue().toUpperCase());
			}
		}

		// sort the modified fields by position
		Collections.sort(sendAction.getModifiedFields(), TerminalPositionContainerComparator.instance());

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

	@Override
	protected TerminalConnection getConnection() {
		return terminalConnection;
	}

	protected ScreensRecognizer getScreensRecognizer() {
		return screensRecognizer;
	}

	public Object getEntity(String entityName, Object... keys) throws EntityNotFoundException {
		Class<?> entityClass = getScreenEntitiesRegistry().getEntityClass(entityName);
		if (entityClass == null) {
			throw (new EntityNotFoundException(MessageFormat.format("Screen entity \"{0}\" not found", entityName)));
		}
		return getEntity(entityClass, keys);
	}

	protected ScreenEntitiesRegistry getScreenEntitiesRegistry() {
		// Fetch registry from application context. Since session is proxied in web apps, the new registry bean is not available
		// after refresh, and required a fresh copy to support new entity classes loading during development
		if (openLegacyProperties.isDesigntime()) {
			return SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		}

		if (this.screenEntitiesRegistry == null) {
			screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);
		}
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

	public void flip() {
		terminalConnection.flip();
		// force update
		terminalConnection.fetchSnapshot();
		notifyModulesAfterAction(null);
	}

}
