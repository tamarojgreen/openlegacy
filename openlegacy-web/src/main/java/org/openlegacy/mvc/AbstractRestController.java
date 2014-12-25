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
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.json.EntitySerializationUtils;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.List;

import javax.imageio.ImageIO;
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

	/**
	 * Whether to enable /login URL calls. Can be overridden from /application.properties
	 */
	private boolean enableLogin = true;

	/**
	 * Whether to enable /login via URL GET calls. Can be overridden from /application.properties
	 */
	private boolean enableGetLogin = true;
	private String uploadDir;

	public Object login(String user, String password, HttpServletResponse response) throws IOException, LoginException {
		if (!enableLogin || !enableGetLogin) {
			throw (new UnsupportedOperationException("/login is not support"));
		}

		try {
			org.openlegacy.modules.login.Login loginModule = getSession().getModule(org.openlegacy.modules.login.Login.class);
			if (loginModule != null) {
				loginModule.login(user, password);
			} else {
				logger.warn("No login module defined. Skipping login");
			}
		} catch (RegistryException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (LoginException e) {
			getSession().disconnect();
			response.resetBuffer();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("text/plain; charset=UTF-8");
			response.getOutputStream().print(e.getMessage());
			throw e;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return getMenu(response);

	}

	protected ModelAndView getEntity(String entityName, boolean children, HttpServletResponse response) throws IOException {
		try {
			return getEntityRequest(entityName, null, children, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	protected ModelAndView getEntityWithKey(String entityName, String key, boolean children, HttpServletResponse response)
			throws IOException {
		try {
			return getEntityRequest(entityName, key, children, response);
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	protected ModelAndView getEntityDefinitions(String entityName, HttpServletResponse response) throws IOException {
		EntityDefinition<?> entityDefinitions = getEntitiesRegistry().get(entityName);
		return new ModelAndView("definitions", "definitions", entityDefinitions);

	}

	protected ModelAndView getEntityRequest(String entityName, String key, boolean children, HttpServletResponse response)
			throws IOException {
		if (!authenticate(response)) {
			return null;
		}
		try {
			Object entity = getApiEntity(entityName, key);
			return getEntityInner(entity, children);
		} catch (EntityNotFoundException e) {
			logger.fatal(e, e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return null;
		}
	}

	protected Object postApiEntity(String entityName, Class<?> entityClass, String key) {
		return getApiEntity(entityName, key);
	}

	protected Object getApiEntity(String entityName, String key) {
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
		Navigation navigationModule = getSession().getModule(Navigation.class);
		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(entity, navigationModule != null ? navigationModule.getPaths()
				: null, getActions(entity));
		return new ModelAndView(MODEL, MODEL, wrapper);
	}

	protected abstract List<ActionDefinition> getActions(Object entity);

	protected Object getMenu(HttpServletResponse response) throws IOException {
		try {
			Menu menuModule = getSession().getModule(Menu.class);
			if (menuModule == null) {
				return null;
			}
			MenuItem menus = menuModule.getMenuTree();
			return menus;

		} catch (RuntimeException e) {
			handleException(response, e);
		}
		return null;
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
		org.openlegacy.modules.login.Login loginModule = getSession().getModule(org.openlegacy.modules.login.Login.class);
		if (!loginModule.isLoggedIn()) {
			sendError(HttpServletResponse.SC_UNAUTHORIZED, "User unauthorized!", response);
		}
		return true;

	}

	public Object loginPostJson(String json, HttpServletResponse response) throws IOException {
		json = decode(json, "{");
		if (!enableLogin) {
			throw (new UnsupportedOperationException("/login is not support"));
		}

		try {
			LoginObject login = EntitySerializationUtils.deserialize(json, LoginObject.class);
			getSession().getModule(org.openlegacy.modules.login.Login.class).login(login.getUser(), login.getPassword());
		} catch (LoginException e) {
			getSession().disconnect();
			sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), response);
		} catch (Exception e) {
			sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid login", response);
			logger.fatal("Invalid login", e);
		}

		return getMenu(response);
	}

	private static void sendError(int errorCode, String message, HttpServletResponse response) throws IOException {
		response.resetBuffer();
		response.setStatus(errorCode);
		response.setHeader("Content-Type", "application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(String.format("{\"error\":\"%s\"}", message));
		response.flushBuffer();
	}

	public Object loginPostXml(String xml, HttpServletResponse response) throws IOException {
		xml = decode(xml, "{");
		if (!enableLogin) {
			throw (new UnsupportedOperationException("/login is not support"));
		}

		try {
			InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
			LoginObject login = (LoginObject)Unmarshaller.unmarshal(LoginObject.class, inputSource);
			getSession().getModule(org.openlegacy.modules.login.Login.class).login(login.getUser(), login.getPassword());
		} catch (LoginException e) {
			getSession().disconnect();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid login");
			logger.fatal("Invalid login", e);
		}
		return getMenu(response);
	}

	protected void uploadImage(MultipartFile file, HttpServletResponse response) throws IOException {
		String fileName = file.getOriginalFilename();
		fileName = fileName.replaceAll(" ", "_");

		if (uploadDir == null) {
			String property = "java.io.tmpdir";
			// Get the temporary directory and print it.
			uploadDir = System.getProperty(property);
		}
		file.transferTo(new File(uploadDir, fileName));

		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		out.print("{\"filename\":\"" + fileName + "\"}");
		out.flush();
	}

	protected void getImage(HttpServletResponse response, String filename) throws IOException {

		if (uploadDir == null) {
			String property = "java.io.tmpdir";
			// Get the temporary directory and print it.
			uploadDir = System.getProperty(property);
		}
		File file = new File(uploadDir, filename);
		BufferedImage bufferedImage = ImageIO.read(file);

		response.setContentType(URLConnection.guessContentTypeFromName(filename));
		OutputStream out = response.getOutputStream();

		String ext = "";

		int i = filename.lastIndexOf('.');
		if (i >= 0) {
			ext = filename.substring(i + 1);
		}

		ImageIO.write(bufferedImage, ext, out);
		out.close();
	}

	public void setRequiresLogin(boolean requiresLogin) {
		this.requiresLogin = requiresLogin;
	}

	public void setEnableLogin(boolean enableLogin) {
		this.enableLogin = enableLogin;
	}

	public void setEnableGetLogin(boolean enableGetLogin) {
		this.enableGetLogin = enableGetLogin;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public static class LoginObject {

		private String user;
		private String password;

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}
}
