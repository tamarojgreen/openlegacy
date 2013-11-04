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
package org.openlegacy.mvc.web;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.Session;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.layout.PageBuilder;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.mvc.OpenLegacyWebProperties;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public abstract class AbstractGenericEntitiesController<S extends Session> {

	protected static final String ACTION = "action";

	private final static Log logger = LogFactory.getLog(AbstractGenericEntitiesController.class);

	private static final String PAGE = "page";

	private static final String MAIN_MENU = "mainMenu";

	@Inject
	private S session;

	protected S getSession() {
		return session;
	}

	@Inject
	private EntitiesRegistry<?, ?, ?> entitiesRegistry;

	@Inject
	private ServletContext servletContext;

	private String mobileViewsPath = "/WEB-INF/mobile/views";

	private String webViewsPath = "/WEB-INF/web/views";

	private String viewsSuffix = ".jspx";

	@Inject
	private OpenLegacyWebProperties openlegacyWebProperties;

	@SuppressWarnings("rawtypes")
	@Inject
	private PageBuilder pageBuilder;

	@RequestMapping(value = "/{entity}", method = RequestMethod.GET)
	public String getEntity(@PathVariable("entity") String entityName,
			@RequestParam(value = "partial", required = false) String partial, Model uiModel, HttpServletRequest request)
			throws IOException {
		if (entityName.equals(MAIN_MENU)) {
			return MvcConstants.ROOTMENU_VIEW;
		}
		return getEntityWithKey(entityName, null, partial, uiModel, request);

	}

	@RequestMapping(value = "/{entity}/{key:[\\w+[-_ ]*\\w+]+}", method = RequestMethod.GET)
	public String getEntityWithKey(@PathVariable("entity") String entityName, @PathVariable("key") String key,
			@RequestParam(value = "partial", required = false) String partial, Model uiModel, HttpServletRequest request)
			throws IOException {

		try {
			// enable sending more then one key, concatenated with "+"
			Object[] keys = new Object[0];
			if (key != null) {
				keys = key.split("\\+");
			}
			Object entity = session.getEntity(entityName, keys);
			return prepareView(entity, uiModel, partial != null, request);
		} catch (RuntimeException e) {
			return handleFallbackUrl(e);
		}
	}

	protected String handleFallbackUrl(RuntimeException e) {
		if (openlegacyWebProperties.isFallbackUrlOnError()) {
			Assert.notNull(openlegacyWebProperties.getFallbackUrl(), "No fallback URL defined");
			logger.error(e.getMessage());
			return MvcConstants.REDIRECT + openlegacyWebProperties.getFallbackUrl();
		} else {
			throw (e);
		}
	}

	protected abstract ActionDefinition findAction(Object entity, String action);

	protected String handleEntity(HttpServletRequest request, Model uiModel, Object resultEntity) throws MalformedURLException {
		if (resultEntity == null) {
			Assert.notNull(openlegacyWebProperties.getFallbackUrl(), "No fallback URL defined");
			return MvcConstants.REDIRECT + openlegacyWebProperties.getFallbackUrl();
		} else {
			boolean isPartial = request.getParameter("partial") != null;
			if (isPartial) {
				return prepareView(resultEntity, uiModel, isPartial, request);
			} else {
				EntityDefinition<?> entityDefinition = entitiesRegistry.get(resultEntity.getClass());
				return MvcConstants.REDIRECT + entityDefinition.getEntityName();
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected String prepareView(Object entity, Model uiModel, boolean partial, HttpServletRequest request)
			throws MalformedURLException {
		String screenEntityName = ProxyUtil.getOriginalClass(entity.getClass()).getSimpleName();
		uiModel.addAttribute(StringUtils.uncapitalize(screenEntityName), entity);
		EntityDefinition<?> entityDefinition = entitiesRegistry.get(screenEntityName);
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
	 * Look for the given entity in the registry, and return HTTP 400 (BAD REQUEST) in case it's not found
	 * 
	 * @param entityName
	 * @param response
	 * @return
	 * @throws IOException
	 */
	protected Class<?> findAndHandleNotFound(String entityName, HttpServletResponse response) throws IOException {
		Class<?> entityClass = entitiesRegistry.getEntityClass(entityName);
		if (entityClass == null) {
			String message = MessageFormat.format("Entity {0} not found", entityName);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
			logger.error(message);
		}
		return entityClass;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		registerPropertyEditors(binder);
	}

	protected static void registerPropertyEditors(DataBinder binder) {
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
