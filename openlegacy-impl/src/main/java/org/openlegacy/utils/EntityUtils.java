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
package org.openlegacy.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;
import org.openlegacy.PojoFieldAccessor;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.terminal.utils.SimplePojoFieldAccessor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class EntityUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(EntityUtils.class);

	/**
	 * For demo's purposes
	 */
	private boolean returnTrueOnDifferentKeys = false;

	/**
	 * Find the all screen fields marked as key=true
	 * 
	 * @param entity
	 * @return
	 */
	public static List<Object> getKeysValues(Object entity, EntityDefinition<?> entityDefinition) {
		List<? extends FieldDefinition> keyFields = entityDefinition.getKeys();
		List<Object> keysValue = new ArrayList<Object>();
		PojoFieldAccessor fieldAccessor = new SimplePojoFieldAccessor(entity);
		for (FieldDefinition fieldDefinition : keyFields) {
			keysValue.add(fieldAccessor.evaluateFieldValue(fieldDefinition.getName()));
		}
		return keysValue;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isEntitiesEquals(EntitiesRegistry entitiesRegistry, Object entity, Class<?> entityClass,
			Object... requestedEntityKeys) {
		if (entity == null) {
			return false;
		}
		if (!ProxyUtil.isClassesMatch(entity.getClass(), entityClass)) {
			return false;
		}

		EntityDefinition entityDefinition = entitiesRegistry.get(entity.getClass());
		List<Object> actualEntityKeysValues = getKeysValues(entity, entityDefinition);
		if (requestedEntityKeys == null && actualEntityKeysValues.size() == 0) {
			return true;
		}

		// it's OK that entity is requested with no keys (sub screen for example within main screen context). False only if
		// request and no/less keys defined on screen
		if (requestedEntityKeys.length > actualEntityKeysValues.size()) {
			return false;
		}

		for (int i = 0; i < requestedEntityKeys.length; i++) {
			Object actualEntityKeyValue = actualEntityKeysValues.get(i);
			if (!actualEntityKeyValue.toString().equals(requestedEntityKeys[i].toString())) {
				if (returnTrueOnDifferentKeys) {
					logger.warn(MessageFormat.format(
							"Request entity key:{0} doesn''t match current entity key:{1}. Skipping as entityUtils.returnTrueOnDifferentKeys was set to true",
							requestedEntityKeys[i], actualEntityKeyValue));
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public String getEntityName(Object entity) {
		String screenEntityName = ProxyUtil.getOriginalClass(entity.getClass()).getSimpleName();
		return screenEntityName;
	}

	public void setReturnTrueOnDifferentKeys(boolean returnTrueOnDifferentKeys) {
		this.returnTrueOnDifferentKeys = returnTrueOnDifferentKeys;
	}
}
