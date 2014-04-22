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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.binders.MultyScreenTableBindUtil;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.ui.Model;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class ScreenBindUtils {

	private final static Log logger = LogFactory.getLog(ScreenBindUtils.class);

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Inject
	private MultyScreenTableBindUtil multyScreenTableBindUtil;

	/**
	 * Collects multiple screens table, and sets it to the view
	 * 
	 * @param entity
	 * @param uiModel
	 * @param session
	 * @param definition
	 */
	public void bindCollectTable(TerminalSession session, Object entity, Model uiModel) {
		Object collectedEntity = multyScreenTableBindUtil.bindCollectTable(session, entity);
		if (collectedEntity != null) {
			String screenEntityName = ProxyUtil.getOriginalClass(entity.getClass()).getSimpleName();
			uiModel.addAttribute(StringUtils.uncapitalize(screenEntityName), collectedEntity);
		}
	}

	/**
	 * Bind table input fields from http request to a screen entity
	 * 
	 * @param request
	 * @param entityClass
	 * @param screenEntity
	 */
	public void bindTables(HttpServletRequest request, Class<?> entityClass, ScreenEntity screenEntity) {
		ScreenEntityDefinition entityDefinition = entitiesRegistry.get(entityClass);
		Collection<ScreenTableDefinition> tableDefinitions = entityDefinition.getTableDefinitions().values();
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		for (ScreenTableDefinition tableDefinition : tableDefinitions) {
			List<ScreenColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
			String fieldName = tableDefinition.getTableEntityName() + "s";
			if (!fieldAccessor.isExists(fieldName)) {
				logger.warn(MessageFormat.format("Unable to find field named:{0} for binding http request", fieldName));
				continue;
			}
			List<?> rows = (List<?>)fieldAccessor.getFieldValue(fieldName);
			int rowNumber = 0;
			for (Object row : rows) {
				ScreenPojoFieldAccessor rowFieldAccessor = new SimpleScreenPojoFieldAccessor(row);
				for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
					if (columnDefinition.isEditable()) {

						String value = request.getParameter(MessageFormat.format("{0}_{1}", columnDefinition.getName(), rowNumber));
						if (value != null) {
							rowFieldAccessor.setFieldValue(columnDefinition.getName(), value);
						}
					}

				}
				rowNumber++;
			}
		}
	}
}
