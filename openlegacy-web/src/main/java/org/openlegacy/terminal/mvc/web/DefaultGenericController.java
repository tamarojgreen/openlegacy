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
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.table.TableWriter;
import org.openlegacy.mvc.OpenLegacyWebProperties;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.json.JsonSerializationUtil;
import org.openlegacy.terminal.layout.ScreenPageBuilder;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;
import org.openlegacy.terminal.providers.TablesDefinitionProvider;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
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
public class DefaultGenericController {

	private static final String ACTION = "action";

	private final static Log logger = LogFactory.getLog(DefaultGenericController.class);

	private static final String PAGE = "page";

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	@Inject
	private ScreenPageBuilder pageBuilder;

	@Inject
	private TablesDefinitionProvider tablesDefinitionProvider;

	@Inject
	private ServletContext servletContext;

	private String mobileViewsPath = "/WEB-INF/mobile/views";

	private String webViewsPath = "/WEB-INF/web/views";

	private String viewsSuffix = ".jspx";

	@Inject
	private TableWriter tableWriter;

	@Inject
	private OpenLegacyWebProperties openlegacyWebProperties;

	@RequestMapping(value = "/{screen}", method = RequestMethod.GET)
	public String getScreenEntity(@PathVariable("screen") String screenEntityName,
			@RequestParam(value = "partial", required = false) String partial, Model uiModel, HttpServletRequest request)
			throws IOException {
		return getScreenEntityWithKey(screenEntityName, null, partial, uiModel, request);

	}

	@RequestMapping(value = "/{screen}/{key:[\\w+[-_ ]*\\w+]+}", method = RequestMethod.GET)
	public String getScreenEntityWithKey(@PathVariable("screen") String screenEntityName, @PathVariable("key") String key,
			@RequestParam(value = "partial", required = false) String partial, Model uiModel, HttpServletRequest request)
			throws IOException {

		try {
			ScreenEntity screenEntity = (ScreenEntity)terminalSession.getEntity(screenEntityName, key);
			return prepareView(screenEntity, uiModel, partial != null, request);
		} catch (RuntimeException e) {
			if (openlegacyWebProperties.isFallbackUrlOnError()) {
				Assert.notNull(openlegacyWebProperties.getFallbackUrl(), "No fallback URL defined");
				logger.fatal(e.getMessage(), e);
				return MvcConstants.REDIRECT + openlegacyWebProperties.getFallbackUrl();
			} else {
				throw (e);
			}
		}

	}

	@RequestMapping(value = "/{screen}", method = RequestMethod.POST)
	public String postScreenEntity(@PathVariable("screen") String screenEntityName,
			@RequestParam(defaultValue = "", value = ACTION) String action,
			@RequestParam(value = "partial", required = false) String partial, HttpServletRequest request,
			HttpServletResponse response, Model uiModel) throws IOException {

		Class<?> entityClass = findAndHandleNotFound(screenEntityName, response);
		if (entityClass == null) {
			return null;
		}

		ScreenEntity screenEntity = (ScreenEntity)terminalSession.getEntity(screenEntityName);
		if (screenEntity == null) {
			ScreenEntity currentEntity = terminalSession.getEntity();
			return handleEntity(request, uiModel, currentEntity);
		}

		ServletRequestDataBinder binder = new ServletRequestDataBinder(screenEntity);
		registerPropertyEditors(binder);
		binder.bind(request);

		TerminalActionDefinition invokedActionDefinition = screenEntityUtils.sendScreenEntity(terminalSession, screenEntity,
				action);

		ScreenEntity resultEntity = null;
		if (invokedActionDefinition != null && invokedActionDefinition.getTargetEntity() != null) {
			resultEntity = (ScreenEntity)terminalSession.getEntity(invokedActionDefinition.getTargetEntity());
		} else {
			resultEntity = terminalSession.getEntity();
		}

		return handleEntity(request, uiModel, resultEntity);
	}

	private String handleEntity(HttpServletRequest request, Model uiModel, ScreenEntity resultEntity)
			throws MalformedURLException {
		if (resultEntity == null) {
			Assert.notNull(openlegacyWebProperties.getFallbackUrl(), "No fallback URL defined");
			return MvcConstants.REDIRECT + openlegacyWebProperties.getFallbackUrl();
		} else {
			boolean isPartial = request.getParameter("partial") != null;
			if (isPartial) {
				return prepareView(resultEntity, uiModel, isPartial, request);
			} else {
				ScreenEntityDefinition entityDefinition = screenEntitiesRegistry.get(resultEntity.getClass());
				return MvcConstants.REDIRECT + entityDefinition.getEntityName();
			}
		}
	}

	private String prepareView(ScreenEntity screenEntity, Model uiModel, boolean partial, HttpServletRequest request)
			throws MalformedURLException {
		String screenEntityName = ProxyUtil.getOriginalClass(screenEntity.getClass()).getSimpleName();
		uiModel.addAttribute(StringUtils.uncapitalize(screenEntityName), screenEntity);
		ScreenEntityDefinition entityDefinition = screenEntitiesRegistry.get(screenEntityName);
		uiModel.addAttribute(PAGE, pageBuilder.build(entityDefinition));

		SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);

		boolean isComposite = entityDefinition.getChildEntitiesDefinitions().size() > 0 && !partial;
		String suffix = isComposite ? MvcConstants.COMPOSITE_SUFFIX : "";
		String viewName = entityDefinition.getEntityName() + suffix;

		String viewsPath = sitePreference == SitePreference.MOBILE ? mobileViewsPath : webViewsPath;

		// check if custom view exists, if not load generic view by characteristics
		if (servletContext.getResource(MessageFormat.format("{0}/{1}{2}", viewsPath, viewName, viewsSuffix)) == null) {
			if (isComposite) {
				// generic composite view (multi tabbed page)
				viewName = MvcConstants.COMPOSITE;
			} else if (entityDefinition.getType() == MenuEntity.class) {
				// generic menu view
				viewName = MvcConstants.ROOTMENU_VIEW;
			} else if (partial) {
				// generic inner page view (inner tab)
				viewName = MvcConstants.GENERIC_VIEW;
			} else if (entityDefinition.isWindow()) {
				// generic window pop-pup view
				viewName = MvcConstants.GENERIC_WINDOW;
			} else {
				// generic view
				viewName = MvcConstants.GENERIC;
			}
		}

		return viewName;
	}

	/**
	 * Look for the given screen entity in the registry, and return HTTP 400 (BAD REQUEST) in case it's not found
	 * 
	 * @param screenEntityName
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private Class<?> findAndHandleNotFound(String screenEntityName, HttpServletResponse response) throws IOException {
		Class<?> entityClass = screenEntitiesRegistry.getEntityClass(screenEntityName);
		if (entityClass == null) {
			String message = MessageFormat.format("Screen entity {0} not found", screenEntityName);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
			logger.error(message);
		}
		return entityClass;
	}

	/**
	 * handle ajax request for load more
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{screen}/more", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> more(@PathVariable("screen") String entityName) {
		// sync the current entity
		terminalSession.getEntity(entityName);
		ScreenEntity nextScreen = terminalSession.doAction(TerminalActions.PAGEDOWN());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");

		Map<String, ScreenTableDefinition> tableDefinitions = tablesDefinitionProvider.getTableDefinitions(nextScreen.getClass());
		if (tableDefinitions.size() == 0) {
			logger.error("Next screen after PAGEDOWN does not contain a table");
			return new ResponseEntity<String>("", headers, HttpStatus.OK);
		}

		List<?> records = ScrollableTableUtil.getSingleScrollableTable(tablesDefinitionProvider, nextScreen);
		String result = new JSONSerializer().serialize(records);
		return new ResponseEntity<String>(result, headers, HttpStatus.OK);
	}

	// export to excel
	@RequestMapping(value = "/{screen}/excel", method = RequestMethod.GET)
	public void excel(@PathVariable("screen") String entityName, HttpServletResponse response) throws IOException {
		ScreenEntity entity = (ScreenEntity)terminalSession.getEntity(entityName);
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
		ScreenEntity screenEntity = (ScreenEntity)terminalSession.getEntity(screenEntityName);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		@SuppressWarnings("unchecked")
		Map<Object, Object> fieldValues = (Map<Object, Object>)ReflectionUtil.invoke(screenEntity,
				MessageFormat.format("get{0}Values", StringUtils.capitalize(fieldName)));

		String result = JsonSerializationUtil.toDojoFormat(fieldValues);
		return new ResponseEntity<String>(result, headers, HttpStatus.OK);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		registerPropertyEditors(binder);
	}

	private static void registerPropertyEditors(DataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	public void setWebViewsPath(String webViewsPath) {
		this.webViewsPath = webViewsPath;
	}

	public void setMobileViewsPath(String mobileViewsPath) {
		this.mobileViewsPath = mobileViewsPath;
	}

	public void setViewsSuffix(String viewsSuffix) {
		this.viewsSuffix = viewsSuffix;
	}

}
