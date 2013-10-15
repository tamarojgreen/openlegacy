/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.mvc.web;

import flexjson.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.modules.table.TableWriter;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.mvc.web.AbstractGenericEntitiesController;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.json.JsonSerializationUtil;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OpenLegacy default web controller for a terminal session. Handles GET/POST requests of a web application. Works closely with
 * generic.jspx / composite.jspx. Saves the need for a dedicated controller and page for each screen API, if such doesn't exists.
 * 
 * @author Roi Mor
 * 
 */
@Controller
public class DefaultGenericScreensController extends AbstractGenericEntitiesController<TerminalSession> {

	private final static Log logger = LogFactory.getLog(DefaultGenericScreensController.class);

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private TableWriter tableWriter;

	@Inject
	private ApplicationContext applicationContext;

	@RequestMapping(value = "/{entity}", method = RequestMethod.POST)
	public String postEntity(@PathVariable("entity") String screenEntityName,
			@RequestParam(defaultValue = "", value = ACTION) String action,
			@RequestParam(value = "partial", required = false) String partial, HttpServletRequest request,
			HttpServletResponse response, Model uiModel) throws IOException {

		Class<?> entityClass = findAndHandleNotFound(screenEntityName, response);
		if (entityClass == null) {
			return null;
		}

		ScreenEntity screenEntity = (ScreenEntity)getSession().getEntity(screenEntityName);
		if (screenEntity == null) {
			ScreenEntity currentEntity = getSession().getEntity();
			return handleEntity(request, uiModel, currentEntity);
		}

		ServletRequestDataBinder binder = new ServletRequestDataBinder(screenEntity);
		registerPropertyEditors(binder);
		binder.bind(request);

		TerminalActionDefinition matchedActionDefinition = screenEntityUtils.findAction(screenEntity, action);
		ScreenEntity resultEntity = getSession().doAction((TerminalAction)matchedActionDefinition.getAction(), screenEntity);
		if (matchedActionDefinition != null && matchedActionDefinition.getTargetEntity() != null) {
			resultEntity = (ScreenEntity)getSession().getEntity(matchedActionDefinition.getTargetEntity());
		}

		return handleEntity(request, uiModel, resultEntity);
	}

	/**
	 * handle ajax request for load more
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{screen}/more", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> more(@PathVariable("screen") String entityName) {
		// sync the current entity
		ScreenEntity screenBefore = (ScreenEntity)getSession().getEntity(entityName);
		ScreenEntity nextScreen = getSession().doAction(TerminalActions.PAGEDOWN());

		TableScrollStopConditions tableScrollStopConditions = SpringUtil.getDefaultBean(applicationContext,
				TableScrollStopConditions.class);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");

		Map<String, ScreenTableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(nextScreen.getClass());
		if (tableDefinitions.size() == 0) {
			logger.error("Next screen after PAGEDOWN does not contain a table");
			return new ResponseEntity<String>("", headers, HttpStatus.OK);
		}

		if (tableScrollStopConditions.shouldStop(screenBefore, nextScreen)) {
			return new ResponseEntity<String>("", headers, HttpStatus.OK);
		}

		List<?> records = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, nextScreen);
		String result = new JSONSerializer().serialize(records);
		return new ResponseEntity<String>(result, headers, HttpStatus.OK);
	}

	// export to excel
	@RequestMapping(value = "/{screen}/excel", method = RequestMethod.GET)
	public void excel(@PathVariable("screen") String entityName, HttpServletResponse response) throws IOException {
		ScreenEntity entity = (ScreenEntity)getSession().getEntity(entityName);
		if (entity == null) {
			return;
		}
		List<?> records = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, entity);
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", MessageFormat.format("attachment; filename=\"{0}.xls\"", entityName));
		tableWriter.writeTable(records, response.getOutputStream());
	}

	/**
	 * handle Ajax request for auto compete fields
	 * 
	 * @param screenEntityName
	 * @param fieldName
	 * @return JSON content
	 */
	@RequestMapping(value = "/{screen}/{field}Values", method = RequestMethod.GET, headers = "X-Requested-With=XMLHttpRequest")
	@ResponseBody
	public ResponseEntity<String> autoCompleteJson(@PathVariable("screen") String screenEntityName,
			@PathVariable("field") String fieldName) {
		ScreenEntity screenEntity = (ScreenEntity)getSession().getEntity(screenEntityName);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		@SuppressWarnings("unchecked")
		Map<Object, Object> fieldValues = (Map<Object, Object>)ReflectionUtil.invoke(screenEntity,
				MessageFormat.format("get{0}Values", StringUtils.capitalize(fieldName)));

		String result = JsonSerializationUtil.toDojoFormat(fieldValues);
		return new ResponseEntity<String>(result, headers, HttpStatus.OK);
	}

	@Override
	protected ActionDefinition findAction(Object entity, String action) {
		return screenEntityUtils.findAction((ScreenEntity)entity, action);
	}

}
