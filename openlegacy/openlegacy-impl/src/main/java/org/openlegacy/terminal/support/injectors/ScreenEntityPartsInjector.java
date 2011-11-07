package org.openlegacy.terminal.support.injectors;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.ScreenEntityFieldAccessor;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.FieldMappingDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.injectors.ScreenEntityDataInjector;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenEntityFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class ScreenEntityPartsInjector implements ScreenEntityDataInjector {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private FieldFormatter fieldFormatter;

	public void inject(ScreenEntityFieldAccessor fieldAccessor, Class<?> screenEntityClass, TerminalScreen terminalScreen,
			boolean deep) {

		Map<String, ScreenPartEntityDefinition> partsDefinitions = screenEntitiesRegistry.get(screenEntityClass).getPartsDefinitions();
		Set<String> fieldPartNames = partsDefinitions.keySet();
		for (String fieldPartName : fieldPartNames) {
			ScreenPartEntityDefinition screenPartEntityDefinition = partsDefinitions.get(fieldPartName);
			Object partObject = ReflectionUtil.newInstance(screenPartEntityDefinition.getPartClass());
			fieldAccessor.setFieldValue(fieldPartName, partObject);

			fieldAccessor = new SimpleScreenEntityFieldAccessor(partObject);

			Collection<FieldMappingDefinition> fieldMappingDefinitions = screenPartEntityDefinition.getFieldsDefinitions().values();
			InjectorUtil.injectFields(fieldAccessor, terminalScreen, fieldMappingDefinitions, fieldFormatter);
		}

	}

	public void setFieldFormatter(FieldFormatter fieldFormatter) {
		this.fieldFormatter = fieldFormatter;
	}

}
