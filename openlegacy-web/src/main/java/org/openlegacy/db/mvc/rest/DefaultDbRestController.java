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
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.json.EntitySerializationUtils;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DefaultDbRestController {

	public static final String JSON = "application/json";
	public static final String XML = "application/xml";
	public static final String USER = "user";
	public static final String PASSWORD = "password";
	protected static final String MODEL = "model";
	protected static final String ACTION = "action";

	@Inject
	private Login dbLoginModule;

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@PersistenceContext
	private EntityManager entityManager;

	private Integer pageSize = 20;

	private Integer pageNumber = 1;

	private Integer pageCount = 0;

	private final static Log logger = LogFactory.getLog(DefaultDbRestController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET, consumes = { JSON, XML })
	public Object login(@RequestParam(USER) String user, @RequestParam(PASSWORD) String password, HttpServletResponse response)
			throws IOException {
		try {
			if (dbLoginModule != null) {
				dbLoginModule.login(user, password);
			} else {
				logger.warn("No login module defined. Skipping login");
			}
		} catch (RegistryException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (LoginException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return null;
	}

	@RequestMapping(value = "/logoff", consumes = { JSON, XML })
	public Object logoff(HttpServletResponse response) throws IOException {
		try {
			if (dbLoginModule != null) {
				dbLoginModule.logoff();
			} else {
				logger.warn("No login module defined. Skipping login");
			}
		} catch (RegistryException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (LoginException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return null;
	}

	@RequestMapping(value = "/{entity}", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getEntity(@PathVariable("entity") String entityName, HttpServletResponse response,
			@RequestParam Map<String, String> allRequestParams) throws IOException {
		try {
			boolean children = false;
			pageNumber = 1;
			if (allRequestParams.containsKey("children")) {
				children = Boolean.parseBoolean(allRequestParams.get("children"));
				allRequestParams.remove("children");
			} else if (allRequestParams.containsKey("page")) {
				pageNumber = Integer.parseInt(allRequestParams.get("page"));
				allRequestParams.remove("page");
			}
			return getEntityRequest(entityName, allRequestParams, null, children, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	// TODO: implement
	@RequestMapping(value = "/{entity}", method = RequestMethod.POST, consumes = JSON)
	public ModelAndView postEntity(@PathVariable("entity") String entityName,
			@RequestParam(value = ACTION, required = false) String action,
			@RequestParam(value = "children", required = false, defaultValue = "true") boolean children,
			@RequestBody String json, HttpServletResponse response) throws IOException {
		return postEntityJson(entityName, action, children, json, response);
	}

	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.GET, consumes = { JSON,
			XML })
	protected ModelAndView getEntityWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = "children", required = false, defaultValue = "true") boolean children,
			HttpServletResponse response) throws IOException {
		try {
			return getEntityRequest(entityName, null, key, children, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	protected ModelAndView getEntityDefinitions(String entityName, HttpServletResponse response) throws IOException {
		EntityDefinition<?> entityDefinitions = dbEntitiesRegistry.get(entityName);
		return new ModelAndView("definitions", "definitions", entityDefinitions);

	}

	protected ModelAndView getEntityRequest(String entityName, Map<String, String> queryConditions, String key, boolean children,
			HttpServletResponse response) throws IOException {
		if (!authenticate(response)) {
			return null;
		}
		Class<?> entityClass = dbEntitiesRegistry.get(entityName).getEntityClass();
		try {
			Object entity = getApiEntity(entityClass, queryConditions, key);
			return getEntityInner(entity, children);
		} catch (EntityNotFoundException e) {
			logger.fatal(e, e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return null;
		}
	}

	protected Object postApiEntity(String entityName, Class<?> entityClass, String key) {
		return getApiEntity(entityClass, null, key);
	}

	protected Object getApiEntity(Class<?> entityClass, Map<String, String> queryConditions, String key) {
		Object[] keys = new Object[0];
		if (key != null) {
			keys = key.split("\\+");
			return entityManager.find(entityClass, Integer.parseInt((String)keys[0]));
		} else {
			if (queryConditions != null && queryConditions.size() != 0) {
				String query = "FROM " + entityClass.getSimpleName() + " WHERE ";
				boolean firstIteration = true;
				for (Entry<String, String> entry : queryConditions.entrySet()) {
					if (!firstIteration) {
						query += " AND ";
					} else {
						firstIteration = false;
					}
					query += entry.getKey() + "=" + entry.getValue();
				}
				return entityManager.createQuery(query).getResultList();
			} else {
				CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
				CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
				countQuery.select(criteriaBuilder.count(countQuery.from(entityClass)));
				Long count = entityManager.createQuery(countQuery).getSingleResult();
				Query query = entityManager.createQuery(String.format("FROM %s", entityClass.getSimpleName()));
				pageCount = (int)Math.ceil((count.intValue() * 1.0) / pageSize);
				if (pageNumber > pageCount) {
					pageNumber = 1;
				}

				if (pageNumber == 1) {
					query.setFirstResult(0);
				} else if (pageNumber <= pageCount) {
					query.setFirstResult(pageSize * (pageNumber - 1));
				}

				query.setMaxResults(pageSize);

				return query.getResultList();
			}
		}
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
		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(entity, null, getActions(entity), pageCount);
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
		Object resultEntity = null;
		if (action == "") {
			resultEntity = entityManager.merge(entity);
		}
		return resultEntity;
	}

	protected boolean authenticate(HttpServletResponse response) throws IOException {
		return true;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
