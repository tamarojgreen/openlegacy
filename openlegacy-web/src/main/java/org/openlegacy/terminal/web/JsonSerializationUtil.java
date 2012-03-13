package org.openlegacy.terminal.web;

import flexjson.JSONSerializer;

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
		jsonRoot.put("identifier", "name");
		jsonRoot.put("items", jsonObjects);
		String result = jsonRoot.toJSONString();
		return result;
	}
}
