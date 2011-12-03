package org.openlegacy.providers.applinx;

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;
import com.sabratec.applinx.common.designtime.model.entity.area.GXScreenAreaPosition;
import com.sabratec.applinx.common.designtime.model.entity.interfaces.GXIEntityDescriptor;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXAbstractFieldMapping;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXAbstractFieldMapping.GXProtection;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXSingleScreenEntity;
import com.sabratec.applinx.common.designtime.model.entity.type.GXSingleScreenType;

import org.openlegacy.FieldType;
import org.openlegacy.exceptions.OpenLegacyProviderException;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class ApxDesignTimeFieldMappingsProvider implements ScreenFieldsDefinitionProvider {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ApxServerLoader apxServerLoader;

	public Collection<ScreenFieldDefinition> getFieldsMappingDefinitions(TerminalSnapshot terminalSnapshot,
			Class<?> screenEntityClass) {

		String screenName = screenEntitiesRegistry.getEntityName(screenEntityClass);
		GXIApplicationContext apxApplication = apxServerLoader.getApplication();

		List<ScreenFieldDefinition> fieldMappingDefinitions = new ArrayList<ScreenFieldDefinition>();

		try {
			GXIEntityDescriptor screenDescriptor = apxApplication.getEntityDescriptorByName(screenName,
					GXSingleScreenType.instance());
			GXSingleScreenEntity screen = (GXSingleScreenEntity)screenDescriptor.fetchEntity();
			List<GXAbstractFieldMapping> apxFieldMappings = screen.getMappings();
			for (GXAbstractFieldMapping apxFieldMapping : apxFieldMappings) {
				if (apxFieldMapping.getArea() instanceof GXScreenAreaPosition) {
					GXScreenAreaPosition positionArea = (GXScreenAreaPosition)apxFieldMapping.getArea();
					TerminalPosition screenPosition = ApxPositionUtil.toScreenPosition(positionArea.getStartPosition());
					SimpleScreenFieldDefinition fieldMappingDefinition = new SimpleScreenFieldDefinition(
							apxFieldMapping.getField().getName(), FieldType.General.class);

					fieldMappingDefinition.setPosition(screenPosition);
					fieldMappingDefinition.setLength(positionArea.getLength());
					fieldMappingDefinition.setEditable(apxFieldMapping.getProtection() != GXProtection.PROTECTED);
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
