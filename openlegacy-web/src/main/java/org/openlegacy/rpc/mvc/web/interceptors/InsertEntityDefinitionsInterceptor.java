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
package org.openlegacy.rpc.mvc.web.interceptors;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.EntityDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.mvc.MvcUtils;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.support.AbstractEntitiesRegistry;
import org.openlegacy.utils.EntityUtils;
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
public class InsertEntityDefinitionsInterceptor extends AbstractRpcInterceptor {

	@Inject
	private RpcEntitiesRegistry entitiesRegistry;

	@Inject
	private MvcUtils mvcUtils;

	@Override
	protected void insertModelData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
		String modelName = StringUtils.uncapitalize(modelAndView.getViewName());
		Object model = modelAndView.getModel().get(modelName);
		if (model != null && model instanceof RpcEntity) {
			mvcUtils.insertModelObjects(modelAndView, model, entitiesRegistry);
		} else {
			PageDefinition page = (PageDefinition)modelAndView.getModel().get("page");
			if (page != null) {

				EntityDefinition<?> definitions = page.getEntityDefinition();
				model = modelAndView.getModel().get(StringUtils.uncapitalize(definitions.getEntityName()));
				if (model != null && model instanceof RpcEntity) {
					// mvcUtils.insertModelObjects(modelAndView, model, definitions);
					modelAndView.addObject("definitions", definitions);
					List<Object> keysValues = EntityUtils.getKeysValues(model, definitions);
					String keysValuesText = StringUtil.toString(keysValues, '_');
					modelAndView.addObject("ol_entityId", keysValuesText);
					modelAndView.addObject("ol_entityUniqueId", definitions.getEntityName() + keysValuesText);
				}
			}
		}
		if (entitiesRegistry.isDirty()) {
			// set the registry back to clean - for design-time purposes only!
			((AbstractEntitiesRegistry<?, ?, ?>)entitiesRegistry).setDirty(false);
		}

		Menu menuModule = getSession().getModule(Menu.class);
		if (menuModule != null) {
			modelAndView.addObject("ol_menu", menuModule.getMenuTree());
			modelAndView.addObject("ol_flatMenus", menuModule.getFlatMenuEntries());
		}
	}

}
