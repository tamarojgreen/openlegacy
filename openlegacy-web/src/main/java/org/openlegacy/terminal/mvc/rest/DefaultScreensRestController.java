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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.Session;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.modules.table.TableWriter;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private TableWriter tableWriter;

	private boolean enableEmulation = true;

	@Inject
	private TerminalSendActionBuilder<HttpServletRequest> sendActionBuilder;

	@Override
	@RequestMapping(value = "/{entity}", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getEntity(@PathVariable("entity") String entityName, HttpServletResponse response) throws IOException {
		return super.getEntity(entityName, response);
	}

	@Override
	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.GET, consumes = { JSON,
			XML })
	public ModelAndView getEntityWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			HttpServletResponse response) throws IOException {
		return super.getEntityWithKey(entityName, key, response);
	}

	@Override
	protected ModelAndView getEntityInner(Object entity) {
		// fetch child entities
		ScreenEntityDefinition entityDefinition = (ScreenEntityDefinition)getEntitiesRegistry().get(entity.getClass());
		List<EntityDefinition<?>> childEntitiesDefinitions = entityDefinition.getChildEntitiesDefinitions();
		if (childEntitiesDefinitions.size() > 0) {
			PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(entity);
			for (PropertyDescriptor propertyDescriptor : properties) {
				for (EntityDefinition<?> childEntityDefinition : childEntitiesDefinitions) {
					if (childEntityDefinition.getEntityName().equalsIgnoreCase(propertyDescriptor.getName())) {
						try {
							propertyDescriptor.getReadMethod().invoke(entity);
						} catch (Exception e) {
							logger.warn(e.getMessage());
						}
					}
				}
			}
		}
		return super.getEntityInner(entity);
	}

	@Override
	@RequestMapping(value = "/{entity}/definitions", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getEntityDefinitions(@PathVariable("entity") String entityName, HttpServletResponse response)
			throws IOException {
		return super.getEntityDefinitions(entityName, response);
	}

	@Override
	@RequestMapping(value = "/menu", method = RequestMethod.GET, consumes = { JSON, XML })
	public Object getMenu(ModelMap model) {
		return super.getMenu(model);
	}

	@Override
	@RequestMapping(value = "/{entity}", method = RequestMethod.POST, consumes = JSON)
	public ModelAndView postEntityJson(@PathVariable("entity") String entityName,
			@RequestParam(value = ACTION, required = false) String action, @RequestBody String json, HttpServletResponse response)
			throws IOException {
		return super.postEntityJson(entityName, action, json, response);
	}

	@Override
	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.POST, consumes = JSON)
	public ModelAndView postEntityJsonWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = ACTION, required = false) String action, @RequestBody String json, HttpServletResponse response)
			throws IOException {
		return super.postEntityJsonWithKey(entityName, key, action, json, response);
	}

	@Override
	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.POST, consumes = XML)
	public ModelAndView postEntityXmlWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = ACTION, required = false) String action, @RequestBody String xml, HttpServletResponse response)
			throws IOException {
		return super.postEntityXmlWithKey(entityName, key, action, xml, response);
	}

	@Override
	@RequestMapping(value = "/{entity}", method = RequestMethod.POST, consumes = XML)
	public ModelAndView postEntityXml(@PathVariable("entity") String entityName,
			@RequestParam(value = ACTION, required = false) String action, @RequestBody String xml, HttpServletResponse response)
			throws IOException {
		return super.postEntityXml(entityName, action, xml, response);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getScreenEntity(HttpServletResponse response) throws IOException {
		if (!authenticate(response)) {
			return null;
		}

		ScreenEntity screenEntity = terminalSession.getEntity();
		return getEntityInner(screenEntity);

	}

	@RequestMapping(value = "/current", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getCurrent(HttpServletResponse response) throws IOException {
		if (terminalSession.getEntity() == null) {
			return null;
		}
		return getEntityRequest(ProxyUtil.getTargetObject(terminalSession.getEntity()).getClass().getSimpleName(), null, response);
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
	public void emulation(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!enableEmulation) {
			logger.warn("emulation for REST controller not enabled");
			return;
		}
		TerminalSendAction action = sendActionBuilder.buildSendAction(terminalSession.getSnapshot(), request);

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

	// export to excel
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{screen}/excel", method = RequestMethod.GET)
	public void excel(@PathVariable("screen") String entityName, HttpServletResponse response) throws IOException {
		ScreenEntity entity = (ScreenEntity)getSession().getEntity(entityName);
		if (entity == null) {
			return;
		}
		List<?> records = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, entity);
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", MessageFormat.format("attachment; filename=\"{0}.xls\"", entityName));
		Entry<String, ScreenTableDefinition> tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, entity.getClass());
		tableWriter.writeTable(records, (TableDefinition)tableDefinition.getValue(), response.getOutputStream());
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
