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
package org.openlegacy.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.Session;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.json.EntitySerializationUtils;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractRestController {

	public static final String JSON = "application/json";
	public static final String XML = "application/xml";
	protected static final String MODEL = "model";
	protected static final String ACTION = "action";

	protected abstract Session getSession();

	protected abstract EntitiesRegistry<?, ?, ?> getEntitiesRegistry();

	private final static Log logger = LogFactory.getLog(AbstractRestController.class);

	/**
	 * Whether to perform login on session start. Can be overridden from /application.properties
	 * defaultRestController.requiresLogin=true
	 */
	private boolean requiresLogin = false;

	protected ModelAndView getEntity(String entityName, HttpServletResponse response) throws IOException {
		return getEntityRequest(entityName, null, response);
	}

	protected ModelAndView getEntityWithKey(String entityName, String key, HttpServletResponse response) throws IOException {
		return getEntityRequest(entityName, key, response);
	}

	protected ModelAndView getEntityDefinitions(String entityName, HttpServletResponse response) throws IOException {
		EntityDefinition<?> entityDefinitions = getEntitiesRegistry().get(entityName);
		return new ModelAndView("definitions", "definitions", entityDefinitions);

	}

	protected ModelAndView getEntityRequest(String entityName, String key, HttpServletResponse response) throws IOException {
		if (!authenticate(response)) {
			return null;
		}
		try {
			Object entity = getApiEntity(entityName, key);
			return getEntityInner(entity);
		} catch (EntityNotFoundException e) {
			logger.fatal(e, e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return null;
		}
	}

	protected Object postApiEntity(String entityName, Class<?> entityClass, String key) {
		return getApiEntity(entityName, key);
	}

	private Object getApiEntity(String entityName, String key) {
		Object entity;
		Object[] keys = new Object[0];
		if (key != null) {
			keys = key.split("\\+");
		}

		if (key == null) {
			entity = getSession().getEntity(entityName);
		} else {
			entity = getSession().getEntity(entityName, keys);
		}
		return entity;
	}

	protected ModelAndView getEntityInner(Object entity) {
		if (entity == null) {
			throw (new EntityNotFoundException("No entity found"));
		}
		entity = ProxyUtil.getTargetObject(entity);
		Navigation navigationModule = getSession().getModule(Navigation.class);
		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(entity, navigationModule != null ? navigationModule.getPaths()
				: null, getActions(entity));
		return new ModelAndView(MODEL, MODEL, wrapper);
	}

	protected abstract List<ActionDefinition> getActions(Object entity);

	protected Object getMenu(ModelMap model) {
		Menu menuModule = getSession().getModule(Menu.class);
		if (menuModule == null) {
			return null;
		}
		MenuItem menus = menuModule.getMenuTree();
		return menus;
	}

	/**
	 * Accepts a post request in JSON format, de-serialize it to a entity, and send it to the host
	 * 
	 * @param entityName
	 * @param action
	 * @param json
	 * @param response
	 *            Return HTTP OK (200) is success
	 * @return
	 * @throws IOException
	 */
	protected ModelAndView postEntityJson(String entityName, String action, String json, HttpServletResponse response)
			throws IOException {
		return postEntityJsonInner(entityName, null, action, json, response);
	}

	protected ModelAndView postEntityJsonWithKey(String entityName, String key, String action, String json,
			HttpServletResponse response) throws IOException {
		return postEntityJsonInner(entityName, key, action, json, response);
	}

	private ModelAndView postEntityJsonInner(String entityName, String key, String action, String json,
			HttpServletResponse response) throws IOException {

		if (!authenticate(response)) {
			return null;
		}

		Class<?> entityClass = findAndHandleNotFound(entityName, response);
		if (entityClass == null) {
			return null;
		}

		Object entity = null;
		postApiEntity(entityName, entityClass, key);

		try {
			entity = EntitySerializationUtils.deserialize(json, entityClass);
		} catch (Exception e) {
			handleDeserializationException(entityName, response, e);
			return null;
		}

		Object resultEntity = sendEntity(entity, action);
		return getEntityInner(resultEntity);
	}

	protected ModelAndView postEntityXmlWithKey(String entityName, String key, String action, String xml,
			HttpServletResponse response) throws IOException {
		return postEntityXmlInner(entityName, key, action, xml, response);
	}

	protected ModelAndView postEntityXml(String entityName, String action, String xml, HttpServletResponse response)
			throws IOException {
		return postEntityXmlInner(entityName, null, action, xml, response);
	}

	private ModelAndView postEntityXmlInner(String entityName, String key, String action, String xml, HttpServletResponse response)
			throws IOException {
		if (!authenticate(response)) {
			return null;
		}

		Class<?> entityClass = findAndHandleNotFound(entityName, response);
		if (entityClass == null) {
			return null;
		}

		Object entity = null;
		postApiEntity(entityName, entityClass, key);

		try {
			InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
			entity = Unmarshaller.unmarshal(entityClass, inputSource);
		} catch (MarshalException e) {
			handleDeserializationException(entityName, response, e);
			return null;
		} catch (ValidationException e) {
			handleDeserializationException(entityName, response, e);
			return null;
		}

		Object resultEntity = sendEntity(entity, action);
		return getEntityInner(resultEntity);
	}

	/**
	 * Look for the given entity in the registry, and return HTTP 400 (BAD REQUEST) in case it's not found
	 * 
	 * @param entityName
	 * @param response
	 * @return
	 * @throws IOException
	 */
	protected Class<?> findAndHandleNotFound(String entityName, HttpServletResponse response) throws IOException {
		Class<?> entityClass = getEntitiesRegistry().getEntityClass(entityName);
		if (entityClass == null) {
			String message = MessageFormat.format("Entity {0} not found", entityName);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
			logger.error(message);
		}
		return entityClass;
	}

	public static void handleDeserializationException(String entityName, HttpServletResponse response, Exception e)
			throws IOException {
		String message = MessageFormat.format("Unable to desirialize entity {0}", entityName);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
		logger.fatal(message, e);
		return;
	}

	protected abstract Object sendEntity(Object entity, String action);

	protected boolean authenticate(HttpServletResponse response) throws IOException {
		if (!requiresLogin) {
			return true;
		}
		Login loginModule = getSession().getModule(Login.class);
		if (!loginModule.isLoggedIn()) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return true;

	}

	public void setRequiresLogin(boolean requiresLogin) {
		this.requiresLogin = requiresLogin;
	}
}
