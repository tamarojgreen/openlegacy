package org.openlegacy.terminal.web;

import flexjson.JSONSerializer;

import org.openlegacy.terminal.TerminalSnapshot;

public class JsonSerializationUtil {

	public static String toJson(TerminalSnapshot terminalSnapshot) {
		JSONSerializer jsonSerializer = new JSONSerializer();
		return jsonSerializer.exclude("*.class").include("fields").include("cursorPosition.*").include("fields.position.*").include(
				"fields.endPosition.*").include("fields.editable").include("fields.hidden").include("fields.value").include(
				"fields.endPosition.*").exclude("*").serialize(terminalSnapshot);
	}
}
