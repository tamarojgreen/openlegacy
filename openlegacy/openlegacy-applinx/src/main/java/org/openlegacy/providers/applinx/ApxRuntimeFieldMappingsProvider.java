package org.openlegacy.providers.applinx;

import com.sabratec.applinx.baseobject.GXIField;
import com.sabratec.applinx.baseobject.GXIFieldCollection;
import com.sabratec.applinx.baseobject.GXIScreen;

import org.openlegacy.FieldType;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldMappingDefinition;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class ApxRuntimeFieldMappingsProvider implements ScreenFieldsDefinitionProvider {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<ScreenFieldDefinition> getFieldsMappingDefinitions(TerminalSnapshot terminalSnapshot,
			Class<?> screenEntityClass) {

		String screenName = screenEntitiesRegistry.getEntityName(screenEntityClass);

		List<ScreenFieldDefinition> fieldMappingDefinitions = new ArrayList<ScreenFieldDefinition>();

		GXIScreen apxScreen = (GXIScreen)terminalSnapshot.getDelegate();

		if (!screenName.equalsIgnoreCase(apxScreen.getName())) {
			throw (new OpenLegacyException(MessageFormat.format("Current screen:{0} doesn''t match request screen:{1}",
					apxScreen.getName(), screenName)));
		}
		GXIFieldCollection apxApplicationFields = apxScreen.getFields().getAppFields();
		for (int i = 0; i < apxApplicationFields.getFieldCount(); i++) {
			GXIField apxField = apxApplicationFields.getFieldAt(i);

			ScreenPosition screenPosition = ApxPositionUtil.toScreenPosition(apxField.getPosition());
			SimpleFieldMappingDefinition fieldMappingDefinition = new SimpleFieldMappingDefinition(apxField.getName(),
					FieldType.General.class);
			fieldMappingDefinition.setLength(apxField.getLength());
			fieldMappingDefinition.setPosition(screenPosition);
			fieldMappingDefinition.setEditable(!apxField.isProtected());
			fieldMappingDefinitions.add(fieldMappingDefinition);
		}
		return fieldMappingDefinitions;
	}
}
