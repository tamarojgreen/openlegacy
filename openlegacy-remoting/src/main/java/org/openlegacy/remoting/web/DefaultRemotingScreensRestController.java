/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.remoting.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.openlegacy.EntityType;
import org.openlegacy.EntityWrapper;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.remoting.terminal.IRemotingTerminalSession;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan Bort
 */
@Controller
public class DefaultRemotingScreensRestController {

	private final static Log logger = LogFactory.getLog(DefaultRemotingScreensRestController.class);

	private static final String JSON = "application/json";
	private static final String XML = "application/xml";

	private static final String USER = "user";
	private static final String PASSWORD = "password";

	private static final String MODEL = "model";

	@Inject
	private IRemotingTerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@RequestMapping(value = "/login", consumes = { JSON, XML })
	public Object login(@RequestParam(USER) String user, @RequestParam(PASSWORD) String password, HttpServletResponse response)
			throws IOException {
		try {
			terminalSession.login(user, password);
		} catch (LoginException e) {
			return null;
		}
		return getMenu(response);
	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET, consumes = { JSON, XML })
	public Object getMenu(HttpServletResponse response) throws IOException {
		try {
			return terminalSession.getMenu();
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login", consumes = { JSON }, method = RequestMethod.POST)
	public Object loginPostJson(@RequestBody String json, HttpServletResponse response) throws IOException {
		try {
			terminalSession.loginPostJson(json);
		} catch (LoginException e) {
			sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), response);
		} catch (Exception e) {
			sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid login", response);
		}

		ScreenEntity entity = terminalSession.getEntity();
		if (entity != null) {
			ScreenEntityDefinition entityDefinition = screenEntitiesRegistry.get(entity.getClass());
			Class<? extends EntityType> screenType = entityDefinition.getType();

			if (!screenType.getSimpleName().equals(MenuEntity.class.getSimpleName())) {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("afterLoginEntityName", entityDefinition.getEntityName());
				return jsonObject;

			} else {
				return getMenu(response);
			}
		}
		return getMenu(response);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.GET, consumes = { JSON, XML })
	public void authenticateUser(HttpServletResponse response) throws IOException {
		try {
			terminalSession.authenticate();
		} catch (LoginException e) {
			sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), response);
		}
	}

	@RequestMapping(value = "/{entity}", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getEntity(@PathVariable("entity") String entityName, @RequestParam(value = "children", required = false,
			defaultValue = "true") boolean children, HttpServletResponse response) throws IOException {
		try {
			EntityWrapper wrapper = terminalSession.getEntityRequest(entityName, null, children);
			return new ModelAndView(MODEL, MODEL, wrapper);
		} catch (LoginException e) {
			sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), response);
		} catch (EntityNotFoundException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
		return null;
	}

	@RequestMapping(value = "/messages", consumes = { JSON, XML })
	public ModelAndView messages() throws IOException {
		List<String> messages = terminalSession.messages();
		return new ModelAndView(MODEL, MODEL, messages);
	}

	protected ModelAndView handleException(HttpServletResponse response, RuntimeException e) throws IOException {
		response.setStatus(500);
		response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
		logger.fatal(e.getMessage(), e);
		return null;
	}

	private static void sendError(int errorCode, String message, HttpServletResponse response) throws IOException {
		response.resetBuffer();
		response.setStatus(errorCode);
		response.setHeader("Content-Type", "application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(String.format("{\"error\":\"%s\"}", message));
		response.flushBuffer();
	}

}
