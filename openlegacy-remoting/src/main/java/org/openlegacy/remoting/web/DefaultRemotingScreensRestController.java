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

import org.json.simple.JSONObject;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityType;
import org.openlegacy.Session;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.remoting.terminal.RemotingTerminalSession;
import org.openlegacy.remoting.web.backend.RemotingBackendScreensRestController;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.mvc.rest.DefaultScreensRestController;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Controller;
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
public class DefaultRemotingScreensRestController extends DefaultScreensRestController {

	//	private final static Log logger = LogFactory.getLog(DefaultRemotingScreensRestController.class);

	private static final String JSON = "application/json";
	private static final String XML = "application/xml";

	@Inject
	private RemotingTerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private RemotingBackendScreensRestController remoteController;

	@Override
	protected Session getSession() {
		return terminalSession;
	}

	@Override
	protected EntitiesRegistry<?, ?, ?> getEntitiesRegistry() {
		return screenEntitiesRegistry;
	}

	@Override
	@RequestMapping(value = "/login", consumes = { JSON, XML })
	public Object login(@RequestParam(Login.USER_FIELD_NAME) String user,
			@RequestParam(Login.PASSWORD_FIELD_NAME) String password, HttpServletResponse response) throws IOException {
		try {
			remoteController.login(user, password);
		} catch (LoginException e) {
			return null;
		}
		return getMenu(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login", consumes = { JSON }, method = RequestMethod.POST)
	public Object loginPostJson(@RequestBody String json, HttpServletResponse response) throws IOException {
		try {
			remoteController.loginPostJson(json);
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

	@Override
	@RequestMapping(value = "/messages", consumes = { JSON, XML })
	public ModelAndView messages() throws IOException {
		List<String> messages = remoteController.messages();
		return new ModelAndView(MODEL, MODEL, messages);
	}

}
