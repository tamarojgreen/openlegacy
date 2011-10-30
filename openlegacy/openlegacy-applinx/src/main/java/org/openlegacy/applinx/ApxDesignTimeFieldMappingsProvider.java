package org.openlegacy.applinx;

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;
import com.sabratec.applinx.common.designtime.model.entity.area.GXScreenAreaPosition;
import com.sabratec.applinx.common.designtime.model.entity.interfaces.GXIEntityDescriptor;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXAbstractFieldMapping;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXSingleScreenEntity;
import com.sabratec.applinx.common.designtime.model.entity.type.GXSingleScreenType;

import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.FieldMappingDefinition;
import org.openlegacy.terminal.FieldMappingsDefinitionProvider;
import org.openlegacy.terminal.ScreenPosition;
import org.openlegacy.terminal.TerminalScreen;
import org.openlegacy.terminal.definitions.SimpleFieldMappingDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApxDesignTimeFieldMappingsProvider implements FieldMappingsDefinitionProvider {

	@Autowired
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Autowired
	private ApxServerLoader apxServerLoader;

	public Collection<FieldMappingDefinition> getFieldsMappingDefinitions(TerminalScreen terminalScreen, Class<?> screenEntity) {

		String screenName = screenEntitiesRegistry.getEntityName(screenEntity);
		GXIApplicationContext apxApplication = apxServerLoader.getApplication();

		List<FieldMappingDefinition> fieldMappingDefinitions = new ArrayList<FieldMappingDefinition>();

		try {
			GXIEntityDescriptor screenDescriptor = apxApplication.getEntityDescriptorByName(screenName,
					GXSingleScreenType.instance());
			GXSingleScreenEntity screen = (GXSingleScreenEntity)screenDescriptor.fetchEntity();
			List<GXAbstractFieldMapping> apxFieldMappings = screen.getMappings();
			for (GXAbstractFieldMapping apxFieldMapping : apxFieldMappings) {
				if (apxFieldMapping.getArea() instanceof GXScreenAreaPosition) {
					GXScreenAreaPosition positionArea = (GXScreenAreaPosition)apxFieldMapping.getArea();
					ScreenPosition screenPosition = ApxPositionUtil.toScreenPosition(positionArea.getStartPosition());
					FieldMappingDefinition fieldMappingDefinition = new SimpleFieldMappingDefinition(
							apxFieldMapping.getField().getName(), screenPosition, apxFieldMapping.getLength());
					fieldMappingDefinitions.add(fieldMappingDefinition);
				} else {
					throw (new UnsupportedOperationException(
							"Only field mappings of type screen area position are supported with ApplinX"));
				}
			}
		} catch (GXDesignTimeException e) {
			throw (new OpenLegacyProviderException(e));
		}
		return fieldMappingDefinitions;
	}

}
