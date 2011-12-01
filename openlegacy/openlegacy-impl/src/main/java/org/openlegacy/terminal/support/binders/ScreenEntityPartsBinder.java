package org.openlegacy.terminal.support.binders;

import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

@Component
public class ScreenEntityPartsBinder implements ScreenEntityBinder {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ScreenBinderLogic screenBinderLogic;

	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) {

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		Map<String, ScreenPartEntityDefinition> partsDefinitions = screenEntitiesRegistry.get(screenEntity.getClass()).getPartsDefinitions();
		Set<String> fieldPartNames = partsDefinitions.keySet();
		for (String fieldPartName : fieldPartNames) {
			ScreenPartEntityDefinition screenPartEntityDefinition = partsDefinitions.get(fieldPartName);
			Object partObject = ReflectionUtil.newInstance(screenPartEntityDefinition.getPartClass());
			fieldAccessor.setFieldValue(fieldPartName, partObject);

			SimpleScreenPojoFieldAccessor partFieldAccessor = new SimpleScreenPojoFieldAccessor(partObject);

			Collection<ScreenFieldDefinition> fieldMappingDefinitions = screenPartEntityDefinition.getFieldsDefinitions().values();
			screenBinderLogic.populatedFields(partFieldAccessor, terminalSnapshot, fieldMappingDefinitions);
		}

	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalSnapshot terminalSnapshot, Object entity) {

		Map<String, ScreenPartEntityDefinition> partsDefinitions = screenEntitiesRegistry.get(entity.getClass()).getPartsDefinitions();

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);

		Set<String> fieldPartNames = partsDefinitions.keySet();
		for (String fieldPartName : fieldPartNames) {
			ScreenPartEntityDefinition screenPartEntityDefinition = partsDefinitions.get(fieldPartName);
			Object screenPart = fieldAccessor.getFieldValue(fieldPartName);
			screenBinderLogic.populateSendAction(sendAction, terminalSnapshot, screenPart,
					screenPartEntityDefinition.getFieldsDefinitions().values());
		}

	}

}
