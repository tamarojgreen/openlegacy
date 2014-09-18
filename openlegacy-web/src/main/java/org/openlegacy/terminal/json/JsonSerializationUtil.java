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
package org.openlegacy.terminal.json;

import flexjson.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JsonSerializationUtil {

	public static String toJson(TerminalSnapshot terminalSnapshot) {
		JSONSerializer jsonSerializer = new JSONSerializer();
		return jsonSerializer.exclude("*.class").include("size.*").include("fields").include("cursorPosition.*").include(
				"fields.position.*").include("fields.endPosition.*").include("fields.editable").include("fields.hidden").include(
				"fields.value").include("fields.endPosition.*").exclude("*").serialize(terminalSnapshot);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String toDojoFormat(Map<Object, Object> map) {
		Set<Entry<Object, Object>> entrySets = map.entrySet();
		JSONObject jsonRoot = new JSONObject();
		List jsonObjects = new ArrayList();
		for (Entry<Object, Object> entry : entrySets) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", entry.getKey());
			jsonObject.put("name", entry.getValue());
			jsonObjects.add(jsonObject);
		}
		jsonRoot.put("identifier", "id");
		jsonRoot.put("items", jsonObjects);
		String result = jsonRoot.toJSONString();
		return result;
	}

	/**
	 * Returns
	 */
	@SuppressWarnings("unchecked")
	public static String toDojoFormat(Map<Object, Object> map, String searchPattern) {
		// remove '*' from searchPattern
		if (searchPattern != null) {
			searchPattern = searchPattern.replace("*", "");
		}

		JSONArray jsonArray = new JSONArray();
		Set<Entry<Object, Object>> entrySets = map.entrySet();
		for (Entry<Object, Object> entry : entrySets) {
			boolean addToArray = false;
			if (StringUtils.isEmpty(searchPattern)) {
				addToArray = true;
			} else {
				String value = (String)entry.getValue();
				if (value.startsWith(searchPattern)) {
					addToArray = true;
				}
			}
			if (addToArray) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", entry.getKey());
				jsonObject.put("name", entry.getValue());
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray.toJSONString();
	}
}
