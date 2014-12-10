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

package org.openlegacy.demo.db.controllers;

import org.openlegacy.db.mvc.rest.DefaultDbRestController;
import org.openlegacy.db.services.DbEntitiesRegistry;
import org.openlegacy.demo.db.model.StockItemHolderPK;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan Bort
 * 
 */
@Controller
@RequestMapping("/StockItemHolder")
public class StockItemHolderController {

	private static final String MODEL = "model";

	@Inject
	private DbEntitiesRegistry dbEntitiesRegistry;

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/{key:[[\\w\\p{L}]+[-_ ]*[\\w\\p{L}]+]+}", method = RequestMethod.GET, consumes = {
			DefaultDbRestController.JSON, DefaultDbRestController.XML })
	public ModelAndView getHolderWithKey(@PathVariable("key") String key, HttpServletResponse response) throws IOException {
		try {
			Class<?> entityClass = dbEntitiesRegistry.get("StockItemHolder").getEntityClass();
			Object[] keys = new Object[0];
			if (key != null) {
				keys = key.split("\\+");
				StockItemHolderPK pk = new StockItemHolderPK();
				pk.setId1(Integer.parseInt((String)keys[0]));
				pk.setId2(Long.parseLong((String)keys[1]));
				Object entity = entityManager.find(entityClass, pk);
				return getEntityInner(entity, true);
			}
		} catch (RuntimeException e) {
			return handleException(response, e);
		}
		return null;
	}

	protected ModelAndView getEntityInner(Object entity, boolean children) {
		if (entity == null) {
			throw (new EntityNotFoundException("No entity found"));
		}

		entity = ProxyUtil.getTargetJpaObject(entity, children);
		// entity = ProxyUtil.getTargetObject(entity, children);
		SimpleEntityWrapper wrapper = new SimpleEntityWrapper(entity, null, null, 0);
		return new ModelAndView(MODEL, MODEL, wrapper);
	}

	private static ModelAndView handleException(HttpServletResponse response, RuntimeException e) throws IOException {
		response.setStatus(500);
		response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
		return null;
	}

}
