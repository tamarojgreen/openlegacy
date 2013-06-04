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
package org.openlegacy.terminal.mvc.web.interceptors;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.mvc.MvcUtils;
import org.openlegacy.support.AbstractEntitiesRegistry;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepter class for spring MVC. Injects commonly used beans into the page context so they can be accessed via the web page
 * 
 * @author RoiM
 * 
 */
public class InsertEntityDefinitionsInterceptor extends AbstractScreensInterceptor {

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Inject
	private MvcUtils mvcUtils;

	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
		if (!getSession().isConnected()) {
			// insert login definitions when not connected
			ScreenEntityDefinition definitions = entitiesRegistry.getSingleEntityDefinition(LoginEntity.class);
			if (definitions != null) {
				modelAndView.addObject("definitions", definitions);
			}
			return;
		}
		TerminalSession terminalSession = getSession();
		ScreenEntity entity = terminalSession.getEntity();

		if (entity != null) {
			ScreenEntityDefinition definitions = entitiesRegistry.get(entity.getClass());

			mvcUtils.insertModelObjects(modelAndView, entity, entitiesRegistry);

			NavigationDefinition navigationDefinition = definitions.getNavigationDefinition();
			if (navigationDefinition != null) {
				modelAndView.addObject("accessedFromDefinitions", entitiesRegistry.get(navigationDefinition.getAccessedFrom()));
			}

		}
		Menu menuModule = terminalSession.getModule(Menu.class);
		if (menuModule != null) {
			modelAndView.addObject("ol_menu", menuModule.getMenuTree());
			modelAndView.addObject("ol_flatMenus", menuModule.getFlatMenuEntries());
		}

		Navigation navigationModule = terminalSession.getModule(Navigation.class);
		if (navigationModule != null) {
			List<EntityDescriptor> breadCrumb = navigationModule.getPaths();
			if (breadCrumb != null) {
				modelAndView.addObject("breadCrumb", breadCrumb);
			}
		}

		if (entitiesRegistry.isDirty()) {
			// set the registry back to clean - for design-time purposes only!
			((AbstractEntitiesRegistry<?, ?>)entitiesRegistry).setDirty(false);
		}

	}
}
