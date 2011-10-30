package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXIField;
import com.sabratec.applinx.baseobject.GXIFieldCollection;
import com.sabratec.applinx.baseobject.GXIScreen;

import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.terminal.FieldMappingDefinition;
import org.openlegacy.terminal.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.SimpleFieldMappingDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApxRuntimeFieldMappingsProvider implements FieldMappingsDefinitionProvider {

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<FieldMappingDefinition> getFieldsMappingDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity) {

		String screenName = screenEntitiesRegistry.getEntityName(screenEntity);

		List<FieldMappingDefinition> fieldMappingDefinitions = new ArrayList<FieldMappingDefinition>();

		GXIScreen apxScreen = (GXIScreen)terminalScreen.getDelegate();

		if (!screenName.equalsIgnoreCase(apxScreen.getName())) {
			throw (new OpenLegacyException(MessageFormat.format("Current host screen:{0} doesn''t match request screen:{1}",
					apxScreen.getName(), screenName)));
		}
		GXIFieldCollection apxApplicationFields = apxScreen.getFields().getAppFields();
		for (int i = 0; i < apxApplicationFields.getFieldCount(); i++) {
			GXIField apxField = apxApplicationFields.getFieldAt(i);

			ScreenPosition screenPosition = ApxPositionUtil.toScreenPosition(apxField.getPosition());
			fieldMappingDefinitions.add(new SimpleFieldMappingDefinition(apxField.getName(), screenPosition, apxField.getLength()));
		}
		return fieldMappingDefinitions;
	}

}
