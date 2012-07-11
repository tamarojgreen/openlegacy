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
package org.openlegacy.terminal.utils;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@Component
public class ScreenEntityUtils {

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	public List<Object> getKeysValues(ScreenEntity entity) {
		ScreenEntityDefinition definitions = entitiesRegistry.get(entity.getClass());
		List<? extends FieldDefinition> keyFields = definitions.getKeys();
		List<Object> keysValue = new ArrayList<Object>();
		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);
		for (FieldDefinition fieldDefinition : keyFields) {
			keysValue.add(fieldAccessor.getFieldValue(fieldDefinition.getName()));
		}
		return keysValue;
	}
}
