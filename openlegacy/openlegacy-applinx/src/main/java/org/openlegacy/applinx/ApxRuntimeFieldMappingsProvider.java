package org.openlegacy.applinx;

import com.sabratec.applinx.baseobject.GXIField;
import com.sabratec.applinx.baseobject.GXIFieldCollection;
import com.sabratec.applinx.baseobject.GXIScreen;

import org.openlegacy.HostEntitiesRegistry;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.terminal.FieldMapping;
import org.openlegacy.terminal.FieldMappingsProvider;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApxRuntimeFieldMappingsProvider implements FieldMappingsProvider {

	@Autowired
	private HostEntitiesRegistry hostEntitiesRegistry;

	public Collection<FieldMapping> getFieldsMappings(TerminalScreen terminalScreen, Class<?> screenEntity) {

		String screenName = hostEntitiesRegistry.get(screenEntity);

		List<FieldMapping> fieldMappings = new ArrayList<FieldMapping>();

		GXIScreen apxScreen = (GXIScreen)terminalScreen.getDelegate();

		if (!screenName.equalsIgnoreCase(apxScreen.getName())) {
			throw (new OpenLegacyException(MessageFormat.format("Current host screen:{0} doesn''t match request screen:{1}",
					apxScreen.getName(), screenName)));
		}
		GXIFieldCollection apxApplicationFields = apxScreen.getFields().getAppFields();
		for (int i = 0; i < apxApplicationFields.getFieldCount(); i++) {
			GXIField apxField = apxApplicationFields.getFieldAt(i);

			ScreenPosition screenPosition = ApxPositionUtil.toScreenPosition(apxField.getPosition());
			fieldMappings.add(new FieldMapping(apxField.getName(), screenPosition, apxField.getLength()));
		}
		return fieldMappings;
	}

}
