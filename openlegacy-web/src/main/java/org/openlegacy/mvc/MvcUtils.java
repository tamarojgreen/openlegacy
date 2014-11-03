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
package org.openlegacy.mvc;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.Session;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.mvc.web.EnumPropertyAdapter;
import org.openlegacy.terminal.mvc.web.ThemeUtil;
import org.openlegacy.utils.EntityUtils;
import org.openlegacy.utils.StringUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.DataBinder;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MvcUtils {

	@Inject
	private ThemeUtil themeUtil;

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Inject
	private OpenLegacyWebProperties openLegacyWebProperties;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertModelObjects(ModelAndView modelAndView, Object entity, EntitiesRegistry entitiesRegistry) {
		if (entity != null) {
			EntityDefinition definitions = entitiesRegistry.get(entity.getClass());
			modelAndView.addObject("definitions", definitions);

			List<Object> keysValues = EntityUtils.getKeysValues(entity, definitions);
			String keysValuesText = StringUtil.toString(keysValues, '_');
			modelAndView.addObject("ol_entityId", keysValuesText);
			modelAndView.addObject("ol_entityUniqueId", definitions.getEntityName() + keysValuesText);
		}
	}

	public void insertGlobalData(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response,
			Session session) {

		themeUtil.applyTheme(modelAndView, request, response);

		modelAndView.addObject("openLegacyProperties", openLegacyProperties);
		modelAndView.addObject("openLegacyWebProperties", openLegacyWebProperties);

		if (session == null || !session.isConnected()) {
			return;
		}

		modelAndView.addObject("ol_connected", true);

	}

	@SuppressWarnings({ "rawtypes" })
	public static void registerEditors(DataBinder binder, EntitiesRegistry entitiesRegistry) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		registerEnumEditors(binder, entitiesRegistry);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerEnumEditors(DataBinder binder, EntitiesRegistry entitiesRegistry) {
		Collection<? extends FieldDefinition> allEnums = entitiesRegistry.getAllFieldsOfType(Enum.class);
		for (FieldDefinition fieldDefinition : allEnums) {
			binder.registerCustomEditor(fieldDefinition.getJavaType(), new EnumPropertyAdapter(fieldDefinition));
		}
	}

}
