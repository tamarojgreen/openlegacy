package org.openlegacy.terminal.json;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenEntityWrapper;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.support.SimpleScreenEntityWrapper;
import org.openlegacy.utils.ProxyUtil;

import java.io.IOException;

public class ScreenEntitySerializationUtils {

	/**
	 * Serialize a screen entity into a screen entity wrapper which contains the entity, it's actions and paths within the
	 * session.
	 * 
	 * @param screenEntity
	 * @param terminalSession
	 * @return a wrapper for the screen entity with additional meta-data
	 */
	public static ScreenEntityWrapper createSerializationContainer(ScreenEntity screenEntity, TerminalSession terminalSession,
			ScreenEntityDefinition entityDefinitions) {
		screenEntity = ProxyUtil.getTargetObject(screenEntity);
		return new SimpleScreenEntityWrapper(screenEntity, terminalSession.getModule(Navigation.class).getPaths(),
				entityDefinitions.getActions());

	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String json, Class<T> entityClass) throws JsonParseException, JsonMappingException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object entity = mapper.readValue(json, entityClass);

		return (T)entity;

	}
}
