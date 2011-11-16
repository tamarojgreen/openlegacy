package org.openlegacy.terminal.support.binders;

import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
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

	public void populateEntity(Object screenEntity, TerminalScreen terminalScreen) {

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		Map<String, ScreenPartEntityDefinition> partsDefinitions = screenEntitiesRegistry.get(screenEntity.getClass()).getPartsDefinitions();
		Set<String> fieldPartNames = partsDefinitions.keySet();
		for (String fieldPartName : fieldPartNames) {
			ScreenPartEntityDefinition screenPartEntityDefinition = partsDefinitions.get(fieldPartName);
			Object partObject = ReflectionUtil.newInstance(screenPartEntityDefinition.getPartClass());
			fieldAccessor.setFieldValue(fieldPartName, partObject);

			SimpleScreenPojoFieldAccessor partFieldAccessor = new SimpleScreenPojoFieldAccessor(partObject);

			Collection<FieldMappingDefinition> fieldMappingDefinitions = screenPartEntityDefinition.getFieldsDefinitions().values();
			screenBinderLogic.populatedFields(partFieldAccessor, terminalScreen, fieldMappingDefinitions);
		}

	}

	public void populateSendAction(TerminalSendAction sendAction, TerminalScreen terminalScreen, Object entity) {

		Map<String, ScreenPartEntityDefinition> partsDefinitions = screenEntitiesRegistry.get(entity.getClass()).getPartsDefinitions();

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);

		Set<String> fieldPartNames = partsDefinitions.keySet();
		for (String fieldPartName : fieldPartNames) {
			ScreenPartEntityDefinition screenPartEntityDefinition = partsDefinitions.get(fieldPartName);
			Object screenPart = fieldAccessor.getFieldValue(fieldPartName);
			screenBinderLogic.populateSendAction(sendAction, terminalScreen, screenPart,
					screenPartEntityDefinition.getFieldsDefinitions().values());
		}

	}

}
