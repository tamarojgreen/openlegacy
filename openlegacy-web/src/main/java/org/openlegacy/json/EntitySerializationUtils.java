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
package org.openlegacy.json;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openlegacy.EntityWrapper;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.support.SimpleEntityWrapper;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.utils.ProxyUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EntitySerializationUtils {

	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

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
		return new SimpleEntityWrapper(entity, navigation != null ? navigation.getPaths() : null, null);

	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String json, Class<T> entityClass) throws JsonParseException, JsonMappingException,
			IOException, IllegalArgumentException, IllegalAccessException, ParseException, InstantiationException,
			ClassNotFoundException {
		Object entity = parseJson(json, entityClass);
		// ObjectMapper mapper = new ObjectMapper();
		// mapper.Object entity = mapper.readValue(json, entityClass);

		return (T)entity;

	}

	private static <T> T parseJson(String jsonRootNode, Class<T> entityClass) throws InstantiationException,
	IllegalAccessException, ClassNotFoundException, ParseException {
		Field[] fields = entityClass.getDeclaredFields();
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject)parser.parse(jsonRootNode);
		Class classDefinition = Class.forName(entityClass.getName());
		Object entity = classDefinition.newInstance();

		for (Field field : fields) {
			if (field.getGenericType() instanceof Collection) {
				Object list = jsonObj.get(field.getName());

			} else {
				field.setAccessible(true);
				// field.set(entity, jsonObj.get(field.getName()));
			}
		}
		return null;
	}

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(String.class);
		return ret;
	}
}
