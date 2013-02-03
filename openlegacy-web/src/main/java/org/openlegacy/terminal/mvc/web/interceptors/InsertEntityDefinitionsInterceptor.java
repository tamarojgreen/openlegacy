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
import org.openlegacy.support.AbstractEntitiesRegistry;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.utils.StringUtil;
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
public class InsertEntityDefinitionsInterceptor extends AbstractInterceptor {

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
		if (!getTerminalSession().isConnected()) {
			// insert login definitions when not connected
			ScreenEntityDefinition definitions = entitiesRegistry.getSingleEntityDefinition(LoginEntity.class);
			if (definitions != null) {
				modelAndView.addObject("definitions", definitions);
			}
			return;
		}
		TerminalSession terminalSession = getTerminalSession();
		ScreenEntity entity = terminalSession.getEntity();

		if (entity != null) {
			ScreenEntityDefinition definitions = entitiesRegistry.get(entity.getClass());
			modelAndView.addObject("definitions", definitions);
			NavigationDefinition navigationDefinition = definitions.getNavigationDefinition();
			if (navigationDefinition != null) {
				modelAndView.addObject("accessedFromDefinitions", entitiesRegistry.get(navigationDefinition.getAccessedFrom()));
			}

			List<Object> keysValues = screenEntityUtils.getKeysValues(entity);
			String keysValuesText = StringUtil.toString(keysValues, '_');
			modelAndView.addObject("ol_entityId", keysValuesText);
			modelAndView.addObject("ol_entityUniqueId", definitions.getEntityName() + keysValuesText);
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

		if (screenEntitiesRegistry.isDirty()) {
			// set the registry back to clean - for design-time purposes only!
			((AbstractEntitiesRegistry<?, ?>)screenEntitiesRegistry).setDirty(false);
		}

	}
}
