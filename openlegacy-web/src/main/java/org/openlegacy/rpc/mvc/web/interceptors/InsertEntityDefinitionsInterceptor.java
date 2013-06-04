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
package org.openlegacy.rpc.mvc.web.interceptors;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.mvc.MvcUtils;
import org.openlegacy.rpc.RpcEntity;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.support.AbstractEntitiesRegistry;
import org.springframework.web.servlet.ModelAndView;

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
		RpcEntity entity = (RpcEntity)modelAndView.getModel().get(modelName);

		mvcUtils.insertModelObjects(modelAndView, entity, entitiesRegistry);

		if (entitiesRegistry.isDirty()) {
			// set the registry back to clean - for design-time purposes only!
			((AbstractEntitiesRegistry<?, ?>)entitiesRegistry).setDirty(false);
		}

	}

}
