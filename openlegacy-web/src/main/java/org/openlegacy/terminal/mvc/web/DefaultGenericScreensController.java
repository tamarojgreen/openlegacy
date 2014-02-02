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
package org.openlegacy.terminal.mvc.web;

import flexjson.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.modules.table.TableWriter;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.mvc.MvcUtils;
import org.openlegacy.mvc.web.AbstractGenericEntitiesController;
import org.openlegacy.mvc.web.MvcConstants;
import org.openlegacy.mvc.web.OpenLegacyViewResolver;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.json.JsonSerializationUtil;
import org.openlegacy.terminal.modules.login.LoginMetadata;
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
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	@Inject
	private LoginMetadata loginMetadata;

	@Inject
	private ScreenBindUtils screenBindUtil;

	@Override
	protected String prepareView(Object entity, Model uiModel, boolean partial, HttpServletRequest request)
			throws MalformedURLException {
		String viewName = super.prepareView(entity, uiModel, partial, request);
		screenBindUtil.bindCollectTable(getSession(), entity, uiModel);
		return viewName;
	}

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
		boolean isPartial = request.getParameter("partial") != null;
		if (screenEntity == null) {
			ScreenEntity currentEntity = getSession().getEntity();
			return handleEntity(request, uiModel, currentEntity, isPartial ? null : MvcConstants.REDIRECT);
		}

		ServletRequestDataBinder binder = new ServletRequestDataBinder(screenEntity);
		MvcUtils.registerEditors(binder, getEntitiesRegistry());
		binder.bind(request);
		screenBindUtil.bindTables(request, entityClass, screenEntity);

		ScreenEntityDefinition beforeEntityDefinition = (ScreenEntityDefinition)getEntitiesRegistry().get(entityClass);
		TerminalActionDefinition matchedActionDefinition = screenEntityUtils.findAction(screenEntity, action);
		ScreenEntity resultEntity = getSession().doAction((TerminalAction)matchedActionDefinition.getAction(), screenEntity);
		if (matchedActionDefinition != null && matchedActionDefinition.getTargetEntity() != null) {
			resultEntity = (ScreenEntity)getSession().getEntity(matchedActionDefinition.getTargetEntity());
		}

		String urlPrefix = isPartial ? null : MvcConstants.REDIRECT;
		if (resultEntity != null) {
			ScreenEntityDefinition afterEntityDefinition = (ScreenEntityDefinition)getEntitiesRegistry().get(
					resultEntity.getClass());
			if (!beforeEntityDefinition.isWindow() && afterEntityDefinition.isWindow()) {
				urlPrefix = OpenLegacyViewResolver.WINDOW_URL_PREFIX;
			}
		}
		return handleEntity(request, uiModel, resultEntity, urlPrefix);
	}

	@Override
	protected String handleFallbackUrl(RuntimeException e) {
		if (!getSession().isConnected() && loginMetadata.getLoginScreenDefinition() != null) {
			logger.error(e.getMessage());
			return MvcConstants.LOGIN_URL;
		} else if (e instanceof ScreenEntityNotAccessibleException) {
			if (getSession().getEntity() != null) {
				ScreenEntityDefinition definition = (ScreenEntityDefinition)getEntitiesRegistry().get(
						getSession().getEntity().getClass());
				return MvcConstants.REDIRECT + definition.getEntityName();
			} else {
				return super.handleFallbackUrl(e);
			}
		} else {
			return super.handleFallbackUrl(e);
		}
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
