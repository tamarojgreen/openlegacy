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
import org.json.simple.JSONObject;
import org.openlegacy.EntityDefinition;
import org.openlegacy.EntityType;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.modules.globals.Globals;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.messages.Messages;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.modules.table.TableWriter;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSendActionBuilder;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.support.binders.MultyScreenTableBindUtil;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class DefaultScreensRestController extends AbstractScreensRestController {

	private static final String JSON = "application/json";
	private static final String XML = "application/xml";

	private final static Log logger = LogFactory.getLog(DefaultScreensRestController.class);

	@Inject
	private MultyScreenTableBindUtil multyScreenTableBindUtil;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private TableWriter tableWriter;

	private boolean enableEmulation = true;

	@Inject
	private TerminalSendActionBuilder<HttpServletRequest> sendActionBuilder;
	private boolean resetRowsWhenSameOnNext = true;
	private boolean enableIncrementalLoading = true;

	private static final String USER = "user";
	private static final String PASSWORD = "password";

	@RequestMapping(value = "/authenticate", method = RequestMethod.GET, consumes = { JSON, XML })
	public void authenticateUser(HttpServletResponse response) throws IOException {
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");
		authenticate(response);
	}

	@Override
	@RequestMapping(value = "/{entity}", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getEntity(@PathVariable("entity") String entityName, @RequestParam(value = "children", required = false,
			defaultValue = "true") boolean children, HttpServletResponse response) throws IOException {
		return super.getEntity(entityName, children, response);
	}

	@Override
	protected Object getApiEntity(String entityName, String key) {
		Object entity = super.getApiEntity(entityName, key);
		if (entity == null) {
			SimpleEntityWrapper wrapper = new SimpleEntityWrapper(null, null, null, false);
			return new ModelAndView(MODEL, MODEL, wrapper);
		}
		return multyScreenTableBindUtil.bindCollectTable(getSession(), entity);
	}

	@Override
	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[:-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.GET, consumes = { JSON,
			XML })
	public ModelAndView getEntityWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = "children", required = false, defaultValue = "true") boolean children,
			HttpServletResponse response) throws IOException {
		return super.getEntityWithKey(entityName, key, children, response);
	}

	@Override
	protected ModelAndView getEntityInner(Object entity, boolean children) {
		if (entity == null) {
			logger.warn("Current screen is not recognized");
			return null;
		}
		// fetch child entities
		ScreenEntityDefinition entityDefinition = (ScreenEntityDefinition) getEntitiesRegistry().get(entity.getClass());
		List<EntityDefinition<?>> childEntitiesDefinitions = entityDefinition.getChildEntitiesDefinitions();
		if (children && childEntitiesDefinitions.size() > 0) {
			PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(entity);
			for (EntityDefinition<?> childEntityDefinition : childEntitiesDefinitions) {
				for (PropertyDescriptor propertyDescriptor : properties) {
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
		return super.getEntityInner(entity, children);
	}

	@Override
	@RequestMapping(value = "/{entity}/definitions", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getEntityDefinitions(@PathVariable("entity") String entityName, HttpServletResponse response)
			throws IOException {
		return super.getEntityDefinitions(entityName, response);
	}

	@Override
	@RequestMapping(value = "/menu", method = RequestMethod.GET, consumes = { JSON, XML })
	public Object getMenu(HttpServletResponse response) throws IOException {
		return super.getMenu(response);
	}

	@Override
	@RequestMapping(value = "/{entity}", method = RequestMethod.POST, consumes = JSON)
	public ModelAndView postEntityJson(@PathVariable("entity") String entityName,
			@RequestParam(value = ACTION, required = false) String action, @RequestParam(value = "children", required = false,
					defaultValue = "true") boolean children, @RequestBody String json, HttpServletResponse response)
			throws IOException {
		return super.postEntityJson(entityName, action, children, json, response);
	}

	/**
	 * Special handling for REST paging next (load more style). If rows haven't changed, do not return table rows in the resulting
	 * JSON
	 * 
	 * @param entityName
	 * @param json
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{entity}", method = RequestMethod.POST, consumes = JSON, params = "action=next")
	public ModelAndView postNextJson(@PathVariable("entity") String entityName, @RequestParam(value = "children",
			required = false, defaultValue = "true") boolean children, @RequestBody String json, HttpServletResponse response)
			throws IOException {

		if (!resetRowsWhenSameOnNext) {
			return super.postEntityJson(entityName, "next", children, json, response);
		}
		preSendJsonEntity(entityName, null, json, response);
		ScreenEntity entity = getSession().getEntity();

		Object resultEntity = sendEntity(entity, "next");

		if (enableIncrementalLoading) {
			List<?> beforeRecords = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, entity);
			List<?> afterRecords = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, resultEntity);
			if (afterRecords.equals(beforeRecords)) {
				afterRecords.clear();
			}
		}
		return getEntityInner(resultEntity, false);
	}

	@Override
	@RequestMapping(value = "/{entity}/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.POST, consumes = JSON)
	public ModelAndView postEntityJsonWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = ACTION, required = false) String action, @RequestParam(value = "children", required = false,
					defaultValue = "true") boolean children, @RequestBody String json, HttpServletResponse response)
			throws IOException {
		return super.postEntityJsonWithKey(entityName, key, action, children, json, response);
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

		ScreenEntity screenEntity = getSession().getEntity();
		return getEntityInner(screenEntity, false);

	}

	@RequestMapping(value = "/current", method = RequestMethod.GET, consumes = { JSON, XML })
	public ModelAndView getCurrent(HttpServletResponse response) throws IOException {
		if (getSession().getEntity() == null) {
			return null;
		}
		return getEntityRequest(ProxyUtil.getTargetObject(getSession().getEntity()).getClass().getSimpleName(), null, false,
				response);
	}

	@RequestMapping(value = "/sequence", method = RequestMethod.GET, consumes = { JSON, XML })
	public Object getSequence(HttpServletResponse response) throws IOException {
		return getSession().getSequence();
	}

	@Override
	@RequestMapping(value = "/login", consumes = { JSON, XML })
	public Object login(@RequestParam(USER) String user, @RequestParam(PASSWORD) String password, HttpServletResponse response)
			throws IOException {
		try {
			super.login(user, password, response);
		} catch (LoginException e) {
			return null;
		}
		return getMenu(response);
	}

	@SuppressWarnings("unchecked")
	@Override
	@RequestMapping(value = "/login", consumes = { JSON }, method = RequestMethod.POST)
	public Object loginPostJson(@RequestBody String json, HttpServletResponse response) throws IOException {
		super.loginPostJson(json, response);

		ScreenEntity entity = getSession().getEntity();
		if (entity != null) {
			ScreenEntityDefinition entityDefinition = getEntitiesRegistry().get(entity.getClass());
			Class<? extends EntityType> screenType = entityDefinition.getType();

			if (!screenType.getSimpleName().equals(MenuEntity.class.getSimpleName())) {

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("afterLoginEntityName", entityDefinition.getEntityName());
				return jsonObject;

			} else {
				return getMenu(response);
			}
		} else {
			return getMenu(response);
		}
	}

	@Override
	@RequestMapping(value = "/login", consumes = { XML }, method = RequestMethod.POST)
	public Object loginPostXml(@RequestBody String xml, HttpServletResponse response) throws IOException {
		return super.loginPostXml(xml, response);
	}

	@RequestMapping(value = "/emulation", consumes = { JSON, XML })
	public ModelAndView emulation(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!enableEmulation) {
			logger.warn("emulation for REST controller not enabled");
			return null;
		}
		TerminalSendAction action = sendActionBuilder.buildSendAction(getSession().getSnapshot(), request);

		getSession().doAction(action);
		ScreenEntity entity = getSession().getEntity();
		Navigation navigationModule = getSession().getModule(Navigation.class);
		boolean isWindow = entity != null ? getEntitiesRegistry().get(entity.getClass()).isWindow() : false;

		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(ProxyUtil.getTargetObject(entity),
				navigationModule != null ? navigationModule.getPaths() : null, getActions(entity), isWindow);
		return new ModelAndView(MODEL, MODEL, wrapper);

	}

	@RequestMapping(value = "/messages", consumes = { JSON, XML })
	public ModelAndView messages() throws IOException {

		Messages messagesModule = getSession().getModule(Messages.class);
		List<String> messages = messagesModule.getMessages();
		if (messages.size() > 0) {
			ModelAndView modelAndView = new ModelAndView(MODEL, MODEL, new ArrayList<String>(messages));
			messagesModule.resetMessages();
			return modelAndView;
		}
		return null;

	}

	@RequestMapping(value = "/globals", consumes = { JSON, XML })
	public ModelAndView globals() throws IOException {

		Globals globalsModule = getSession().getModule(Globals.class);
		Map<String, Object> globals = globalsModule.getGlobals();
		if (globals.size() > 0) {
			ModelAndView modelAndView = new ModelAndView(MODEL, MODEL, globals);
			return modelAndView;
		}
		return null;

	}

	// export to excel
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{screen}/excel", method = RequestMethod.GET)
	public void excel(@PathVariable("screen") String entityName, HttpServletResponse response) throws IOException {
		ScreenEntity entity = (ScreenEntity) getSession().getEntity(entityName);
		if (entity == null) {
			return;
		}
		List<?> records = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, entity);
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", MessageFormat.format("attachment; filename=\"{0}.xls\"", entityName));
		Entry<String, ScreenTableDefinition> tableDefinition = ScrollableTableUtil.getSingleScrollableTableDefinition(
				tablesDefinitionProvider, entity.getClass());
		tableWriter.writeTable(records, (TableDefinition) tableDefinition.getValue(), response.getOutputStream());
	}

	@Override
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public void uploadImage(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
		super.uploadImage(file, response);
	}

	@Override
	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public void getImage(HttpServletResponse response, @RequestParam(value = "filename", required = true) String filename)
			throws IOException {
		super.getImage(response, filename);
	}

	public void setEnableEmulation(boolean enableEmulation) {
		this.enableEmulation = enableEmulation;
	}

	public void setResetRowsWhenSameOnNext(boolean resetRowsWhenSameOnNext) {
		this.resetRowsWhenSameOnNext = resetRowsWhenSameOnNext;
	}

	/**
	 * Determine whether client incrementally load more data, so server should not return existing results
	 * 
	 * @param enableIncrementalLoading
	 */
	public void setEnableIncrementalLoading(boolean enableIncrementalLoading) {
		this.enableIncrementalLoading = enableIncrementalLoading;
	}

}
