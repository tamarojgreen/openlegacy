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
package org.openlegacy.terminal.mvc.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * OpenLegacy default REST API controller. Handles GET/POST requests in the format of JSON or XML. Also handles login /logoff of
 * the host session
 * 
 * @author Roi Mor
 * 
 */
@Controller
public class DefaultScreensRestController extends AbstractRestController {

	private static final String JSON = "application/json";
	private static final String XML = "application/xml";

	private static final String USER = "user";
	private static final String PASSWORD = "password";

	private final static Log logger = LogFactory.getLog(DefaultScreensRestController.class);

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	private boolean enableEmulation = true;

	@RequestMapping(value = "/", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getScreenEntity(HttpServletResponse response) throws IOException {
		if (!authenticate(response)) {
			return null;
		}

		ScreenEntity screenEntity = terminalSession.getEntity();
		return getEntityInner(screenEntity);

	}

	@RequestMapping(value = "/login", consumes = { JSON, XML })
	public void login(@RequestParam(USER) String user, @RequestParam(PASSWORD) String password, HttpServletResponse response)
			throws IOException {
		try {
			Login loginModule = getSession().getModule(Login.class);
			if (loginModule != null) {
				loginModule.login(user, password);
			} else {
				logger.warn("No login module defined. Skipping login");
			}
		} catch (RegistryException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (LoginException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);

	}

	@RequestMapping(value = "/emulation", consumes = { JSON, XML })
	public void emulation(@RequestParam(value = "keyboardKey", required = true) String keyboardKey, HttpServletResponse response)
			throws IOException {
		if (!enableEmulation) {
			logger.warn("emulation for REST controller not enabled");
			return;
		}
		TerminalAction action = TerminalActions.getAction(keyboardKey);

		if (action == null) {
			logger.error(MessageFormat.format("No action found for keyboardKey {0}", keyboardKey));
			return;
		}

		terminalSession.doAction(action);
		response.setStatus(HttpServletResponse.SC_OK);

	}

	@RequestMapping(value = "/messages", consumes = { JSON, XML })
	public ModelAndView messages() throws IOException {

		Messages messagesModule = terminalSession.getModule(Messages.class);
		List<String> messages = messagesModule.getMessages();
		if (messages.size() > 0) {
			ModelAndView modelAndView = new ModelAndView(MODEL, MODEL, new ArrayList<String>(messages));
			messagesModule.resetMessages();
			return modelAndView;
		}
		return null;

	}

	@Override
	protected Session getSession() {
		return terminalSession;
	}

	@Override
	protected EntitiesRegistry<?, ?, ?> getEntitiesRegistry() {
		return screenEntitiesRegistry;
	}

	@Override
	protected Object sendEntity(Object entity, String action) {
		TerminalActionDefinition actionDefinition = screenEntityUtils.findAction((ScreenEntity)entity, action);
		return terminalSession.doAction((TerminalAction)actionDefinition.getAction(), (ScreenEntity)entity);
	}

	@Override
	protected List<ActionDefinition> getActions(Object entity) {
		// actions for screen exists on the entity. No need to fetch from registry
		return null;
	}

}
