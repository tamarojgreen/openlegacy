package org.openlegacy.providers.applinx;

import com.sabratec.applinx.common.designtime.exceptions.GXDesignTimeException;
import com.sabratec.applinx.common.designtime.model.GXEntityDescriptor;
import com.sabratec.applinx.common.designtime.model.GXIApplicationContext;
import com.sabratec.applinx.common.designtime.model.entity.area.GXScreenAreaAnywhere;
import com.sabratec.applinx.common.designtime.model.entity.area.GXScreenAreaPositionLength;
import com.sabratec.applinx.common.designtime.model.entity.interfaces.GXIEntity;
import com.sabratec.applinx.common.designtime.model.entity.interfaces.GXIEntityType;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXFieldEntity;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXFieldMapping;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXSingleScreenEntity;
import com.sabratec.applinx.common.designtime.model.entity.screen.GXTextIdentifier;
import com.sabratec.applinx.common.designtime.model.entity.type.GXFieldType;
import com.sabratec.applinx.common.designtime.model.entity.type.GXSingleScreenType;
import com.sabratec.util.GXPosition;

import org.openlegacy.providers.applinx.ApxServerLoader;

public class ApxTestUtil {

	public static GXIEntity createScreen(ApxServerLoader apxServerLoader,
			String screenName, String identifierText)
			throws GXDesignTimeException {
		GXIApplicationContext apxApplicationContext = getApxContext(apxServerLoader);

		GXEntityDescriptor descriptor = createDescriptor(screenName,
				apxApplicationContext, GXSingleScreenType.instance());

		GXSingleScreenEntity entity = (GXSingleScreenEntity) GXSingleScreenType
				.instance().newEntity();
		entity.setDescriptor(descriptor);

		GXTextIdentifier textIdentifier = new GXTextIdentifier();
		textIdentifier.setArea(new GXScreenAreaAnywhere());
		textIdentifier.setMatch(true);
		textIdentifier.setCaseSensitive(!textIdentifier.isCaseSensitive());
		textIdentifier.setEmpty(false);
		textIdentifier.setText(identifierText);
		entity.getIdentifiers().add(textIdentifier);

		return apxApplicationContext.createEntity(entity);
	}

	private static GXIApplicationContext getApxContext(ApxServerLoader apxServerLoader) {
		GXIApplicationContext apxApplicationContext = apxServerLoader.getServer()
				.getApplications().get(0);
		return apxApplicationContext;
	}

	public static void createField(ApxServerLoader apxServerLoader,
			GXSingleScreenEntity screen, String fieldName, int row, int col,
			int length) throws GXDesignTimeException {
		GXIApplicationContext apxApplicationContext = getApxContext(apxServerLoader);

		GXEntityDescriptor descriptor = createDescriptor(fieldName,
				apxApplicationContext, GXFieldType.instance());
		GXFieldEntity entity = (GXFieldEntity) GXFieldType.instance()
				.newEntity();
		entity.setDescriptor(descriptor);

		GXFieldMapping mapping = new GXFieldMapping();
		mapping.setField(descriptor);
		mapping.setArea(new GXScreenAreaPositionLength(new GXPosition(row, col), length));
		screen.getMappings().add(mapping);

		apxApplicationContext.createEntity(entity);
	}

	private static GXEntityDescriptor createDescriptor(String fieldName,
			GXIApplicationContext apxApplicationContext, GXIEntityType type)
			throws GXDesignTimeException {
		GXEntityDescriptor descriptor = new GXEntityDescriptor(apxApplicationContext, apxApplicationContext
				.createGuid(), fieldName, type, apxApplicationContext.getRoot()
				.getGuid());
		return descriptor;
	}
}
