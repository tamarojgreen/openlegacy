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
package org.openlegacy.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.ApplicationConnection;
import org.openlegacy.ApplicationConnectionListener;
import org.openlegacy.Session;
import org.openlegacy.SessionProperties;
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.utils.SimpleRpcPojoFieldAccessor;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * An abstract session class which exposes modules management functionality
 * 
 * 
 */
public abstract class AbstractSession implements Session, InitializingBean, DisposableBean, Serializable {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(AbstractSession.class);

	private SessionModules sessionModules;

	@Inject
	private SessionPropertiesProvider sessionPropertiesProvider;

	private SessionProperties sessionProperties;

	@Override
	@SuppressWarnings("unchecked")
	public <M extends SessionModule> M getModule(Class<M> module) {
		if (sessionModules == null) {
			throw (new OpenLegacyRuntimeException("No modules defined for session"));
		}
		List<? extends SessionModule> modules = sessionModules.getModules();
		for (SessionModule registeredModule : modules) {
			if (module.isAssignableFrom(registeredModule.getClass())) {
				return (M)registeredModule;
			}
		}
		logger.warn(MessageFormat.format("No module {0} defined for session", module));
		return null;
	}

	public void setSessionModules(SessionModules sessionModules) {
		this.sessionModules = sessionModules;
	}

	public SessionModules getSessionModules() {
		return sessionModules;
	}

	public void setSessionPropertiesProvider(SessionPropertiesProvider sessionPropertiesProvider) {
		this.sessionPropertiesProvider = sessionPropertiesProvider;
	}

	protected abstract ApplicationConnection<?, ?> getConnection();

	protected void notifyModulesBeforeConnect() {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof ApplicationConnectionListener) {
				((ApplicationConnectionListener)sessionModule).beforeConnect(getConnection());
			}
		}
	}

	protected void notifyModulesAfterConnect() {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof ApplicationConnectionListener) {
				((ApplicationConnectionListener)sessionModule).afterConnect(getConnection());
			}
		}
	}

	@Override
	public SessionProperties getProperties() {
		if (sessionProperties == null) {
			try {
				sessionProperties = sessionPropertiesProvider.getSessionProperties();
			} catch (OpenLegacyException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return sessionProperties;
	}

	public void setProperties(SessionProperties sessionProperties) {
		this.sessionProperties = sessionProperties;
	}

	/**
	 * Pass the session to all the modules
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void afterPropertiesSet() throws Exception {
		List<? extends SessionModule> modules = sessionModules.getModules();
		for (SessionModule sessionModule : modules) {
			if (sessionModule instanceof SessionModuleAdapter) {
				((SessionModuleAdapter)sessionModule).setSession(this);
			}
		}
	}

	@Override
	public void destroy() throws Exception {
		disconnect();
	}

	private String defaultInvalidMessage = "is invalid";

	public void validateFields(Object entity, Object entityDefinition) {
		if (entity instanceof ScreenEntity) {
			for (FieldDefinition fieldDefinition : ((ScreenEntityDefinition)entityDefinition).getSortedFields()) {
				if (fieldDefinition.required() || !StringUtils.isEmpty(fieldDefinition.getRegularExpression())) {
					String fieldValue = "";
					ScreenEntity screenEntity = (ScreenEntity)entity;
					SimpleScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);
					Object _fieldValue = fieldAccessor.getFieldValue(fieldDefinition.getName());
					if (_fieldValue instanceof String) {
						fieldValue = (String)_fieldValue;
					} else if (_fieldValue instanceof Integer) {
						fieldValue = ((Integer)_fieldValue).toString();
					} else if (_fieldValue instanceof Double) {
						fieldValue = ((Double)_fieldValue).toString();
					}

					validateField(fieldDefinition.required(), fieldDefinition.getRegularExpression(),
							fieldDefinition.getInvalidMessage(), fieldDefinition.getName(), fieldValue);
				}
			}
		} else if (entity instanceof RpcEntity) {
			for (FieldDefinition fieldDefinition : ((RpcEntityDefinition)entityDefinition).getFieldsDefinitions().values()) {
				if (fieldDefinition.required() || !StringUtils.isEmpty(fieldDefinition.getRegularExpression())) {
					String fieldValue = "";
					RpcEntity rpcEntity = (RpcEntity)entity;
					SimpleRpcPojoFieldAccessor fieldAccessor = new SimpleRpcPojoFieldAccessor(rpcEntity);
					Object _fieldValue = fieldAccessor.getFieldValue(fieldDefinition.getName());
					if (_fieldValue instanceof String) {
						fieldValue = (String)_fieldValue;
					} else if (_fieldValue instanceof Integer) {
						fieldValue = ((Integer)_fieldValue).toString();
					} else if (_fieldValue instanceof Double) {
						fieldValue = ((Double)_fieldValue).toString();
					}

					validateField(fieldDefinition.required(), fieldDefinition.getRegularExpression(),
							fieldDefinition.getInvalidMessage(), fieldDefinition.getName(), fieldValue);
				}
			}
		}
	}

	public void validateField(Boolean required, String regularExpression, String invalidMessage, String fieldName,
			String fieldValue) {
		if (required && StringUtils.isEmpty(fieldValue)) {
			throw new OpenLegacyRuntimeException(fieldName + " is required");
		} else if (!StringUtils.isEmpty(regularExpression) && !StringUtils.isEmpty(fieldValue)) {
			if (!fieldValue.matches(formatRegex(regularExpression))) {
				String _invalidMessage = !StringUtils.isEmpty(invalidMessage) ? invalidMessage
						: fieldName + " " + defaultInvalidMessage;

				throw new OpenLegacyRuntimeException(_invalidMessage);
			}
		}
	}

	@SuppressWarnings("static-method")
	private String formatRegex(String regex) {
		if (regex.matches("^/.*/$")) {
			return regex.substring(1, regex.length() - 1);
		} else {
			return regex;
		}
	}
}
