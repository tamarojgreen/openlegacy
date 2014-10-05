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
package org.openlegacy.db.mvc.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.openlegacy.EntityDefinition;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.json.EntitySerializationUtils;
import org.openlegacy.modules.login.Login;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DefaultDbRestController {

	public static final String JSON = "application/json";
	public static final String XML = "application/xml";
	protected static final String MODEL = "model";
	protected static final String ACTION = "action";

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@PersistenceContext
	private EntityManager entityManager;

	private Login login;

	private final static Log logger = LogFactory.getLog(DefaultDbRestController.class);
	private static final String USER = "user";
	private static final String PASSWORD = "password";

	@RequestMapping(value = "/login", consumes = { JSON, XML })
	public void login(@RequestParam(USER) String user, @RequestParam(PASSWORD) String password, HttpServletResponse response)
			throws IOException, LoginException {
		if (login != null) {
			login.login(user, password);
		} else {
			throw (new LoginException("Login not configured for DB REST controler"));
		}
	}

	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.GET, consumes = { JSON,
			XML })
	protected ModelAndView getEntityWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = "children", required = false, defaultValue = "true") boolean children,
			HttpServletResponse response) throws IOException {
		if (!authenticate(response)) {
			return null;
		}

		try {
			return getEntityRequest(entityName, key, children, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	protected ModelAndView getEntityDefinitions(String entityName, HttpServletResponse response) throws IOException {
		EntityDefinition<?> entityDefinitions = dbEntitiesRegistry.get(entityName);
		return new ModelAndView("definitions", "definitions", entityDefinitions);

	}

	protected ModelAndView getEntityRequest(String entityName, String key, boolean children, HttpServletResponse response)
			throws IOException {
		if (!authenticate(response)) {
			return null;
		}
		Class<?> entityClass = dbEntitiesRegistry.get(entityName).getEntityClass();
		try {
			Object entity = getApiEntity(entityClass, key);
			return getEntityInner(entity, children);
		} catch (EntityNotFoundException e) {
			logger.fatal(e, e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return null;
		}
	}

	protected Object postApiEntity(String entityName, Class<?> entityClass, String key) {
		return getApiEntity(entityClass, key);
	}

	protected Object getApiEntity(Class<?> entityClass, String key) {
		Object entity;
		Object[] keys = new Object[0];
		if (key != null) {
			keys = key.split("\\+");
		}

		entity = entityManager.find(entityClass, Integer.parseInt((String)keys[0])); // TODO
		return entity;
	}

	/**
	 * 
	 * @param entity
	 * @param child
	 *            whether to include child entities
	 * @return
	 */
	protected ModelAndView getEntityInner(Object entity, boolean children) {
		if (entity == null) {
			throw (new EntityNotFoundException("No entity found"));
		}
		entity = ProxyUtil.getTargetObject(entity, children);
		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(entity, null, getActions(entity));
		return new ModelAndView(MODEL, MODEL, wrapper);
	}

	protected List<ActionDefinition> getActions(Object entity) {
		return new ArrayList<ActionDefinition>();
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
	protected ModelAndView postEntityJson(String entityName, String action, boolean children, String json,
			HttpServletResponse response) throws IOException {
		try {
			return postEntityJsonInner(entityName, null, action, children, json, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	private static ModelAndView handleException(HttpServletResponse response, RuntimeException e) throws IOException {
		response.setStatus(500);
		response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
		logger.fatal(e.getMessage(), e);
		return null;
	}

	protected ModelAndView postEntityJsonWithKey(String entityName, String key, String action, boolean children, String json,
			HttpServletResponse response) throws IOException {
		try {
			return postEntityJsonInner(entityName, key, action, children, json, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	private ModelAndView postEntityJsonInner(String entityName, String key, String action, boolean children, String json,
			HttpServletResponse response) throws IOException {

		Object entity = preSendJsonEntity(entityName, key, json, response);

		Object resultEntity = sendEntity(entity, action);
		return getEntityInner(resultEntity, children);
	}

	protected Object preSendJsonEntity(String entityName, String key, String json, HttpServletResponse response)
			throws IOException {
		json = decode(json, "{");

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
			if (json.length() == 0) {
				json = "{}";
			}
			entity = EntitySerializationUtils.deserialize(json, entityClass);
		} catch (Exception e) {
			handleDeserializationException(entityName, response, e);
			return null;
		}
		return entity;
	}

	protected ModelAndView postEntityXmlWithKey(String entityName, String key, String action, String xml,
			HttpServletResponse response) throws IOException {
		try {
			return postEntityXmlInner(entityName, key, action, xml, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	protected ModelAndView postEntityXml(String entityName, String action, String xml, HttpServletResponse response)
			throws IOException {
		try {
			return postEntityXmlInner(entityName, null, action, xml, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	private ModelAndView postEntityXmlInner(String entityName, String key, String action, String xml, HttpServletResponse response)
			throws IOException {

		xml = decode(xml, "<");

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
		return getEntityInner(resultEntity, false);
	}

	private static String decode(String content, String encodeIndicator) {
		URI uri = null;
		try {
			if (content.length() > 0 && !content.contains(encodeIndicator)) {
				uri = new URI(content);
				content = uri.getPath();
			}
		} catch (URISyntaxException e) {
			throw (new RuntimeException(e));
		}
		return content;
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
		Class<?> entityClass = dbEntitiesRegistry.getEntityClass(entityName);
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

	protected Object sendEntity(Object entity, String action) {
		// TODO
		return action;
	}

	protected boolean authenticate(HttpServletResponse response) throws IOException {
		if (login != null && !login.isLoggedIn()) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		return true;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
}
