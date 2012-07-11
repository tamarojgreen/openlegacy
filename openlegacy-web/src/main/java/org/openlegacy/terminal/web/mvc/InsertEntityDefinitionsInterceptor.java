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
package org.openlegacy.terminal.web.mvc;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.inject.Inject;

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

	@Override
	protected void insertModelData(ModelAndView modelAndView) {
		TerminalSession terminalSession = getTerminalSession();
		ScreenEntity entity = terminalSession.getEntity();

		if (entity != null) {
			ScreenEntityDefinition definitions = entitiesRegistry.get(entity.getClass());
			modelAndView.addObject("definitions", definitions);

			List<Object> keysValues = screenEntityUtils.getKeysValues(entity);
			String keysValuesText = StringUtil.toString(keysValues, '_');
			modelAndView.addObject("entityId", keysValuesText);
			modelAndView.addObject("entityUniqueId", definitions.getEntityName() + keysValuesText);
		}
		Menu menuModule = terminalSession.getModule(Menu.class);
		if (menuModule != null) {
			MenuItem menuRoot = menuModule.getMenuTree();
			modelAndView.addObject("menu", menuRoot);
		}

		Navigation navigationModule = terminalSession.getModule(Navigation.class);
		if (navigationModule != null) {
			List<EntityDescriptor> breadCrumb = navigationModule.getPaths();
			if (breadCrumb != null) {
				modelAndView.addObject("breadCrumb", breadCrumb);
			}
		}

	}
}
