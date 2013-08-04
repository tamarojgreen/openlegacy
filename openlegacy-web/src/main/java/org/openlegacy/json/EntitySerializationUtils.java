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
package org.openlegacy.json;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openlegacy.EntityWrapper;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.utils.ProxyUtil;

import java.io.IOException;

public class EntitySerializationUtils {

	/**
	 * Serialize a screen entity into a screen entity wrapper which contains the entity, it's actions and paths within the
	 * session.
	 * 
	 * @param entity
	 * @param session
	 * @return a wrapper for the screen entity with additional meta-data
	 */
	public static EntityWrapper createSerializationContainer(ScreenEntity entity, TerminalSession session,
			ScreenEntityDefinition entityDefinitions) {
		entity = ProxyUtil.getTargetObject(entity);
		Navigation navigation = session.getModule(Navigation.class);
		return new SimpleEntityWrapper(entity, navigation != null ? navigation.getPaths() : null, entityDefinitions.getActions());

	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String json, Class<T> entityClass) throws JsonParseException, JsonMappingException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object entity = mapper.readValue(json, entityClass);

		return (T)entity;

	}
}
