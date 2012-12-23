package org.openlegacy.terminal.modules.globals;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.modules.globals.Globals;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.TerminalSessionModuleAdapter;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DefaultGlobalsModule extends TerminalSessionModuleAdapter implements Globals {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> globals = new HashMap<String, Object>();

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public Map<String, Object> getGlobals() {
		return globals;
	}

	public Object getGlobal(String name) {
		return globals.get(name);
	}

	@Override
	public void afterConnect(TerminalConnection terminalConnection) {
		collectGlobals();
	}

	@Override
	public void afterSendAction(TerminalConnection terminalConnection) {
		collectGlobals();
	}

	private void collectGlobals() {
		ScreenEntity currentEntity = getSession().getEntity();
		if (currentEntity == null) {
			return;
		}

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(currentEntity);

		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(currentEntity.getClass());

		List<? extends FieldDefinition> globalFields = entityDefinitions.getFieldDefinitions(Globals.GlobalField.class);

		for (FieldDefinition fieldDefinition : globalFields) {
			String globalFieldName = fieldDefinition.getName();
			Object globalFieldValue = fieldAccessor.getFieldValue(globalFieldName);
			if (globalFieldValue != null) {
				globals.put(globalFieldName, globalFieldValue);
			}
		}
	}
}
