/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.support.binders;

import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.terminal.ScreenEntityBinder;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ReflectionUtil;
import org.openlegacy.utils.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class ScreenEntityPartsBinder implements ScreenEntityBinder, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient ApplicationContext applicationContext;

	@Inject
	private ScreenBinderLogic screenBinderLogic;

	public void populateEntity(Object screenEntity, TerminalSnapshot terminalSnapshot) {

		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(screenEntity);

		Map<String, PartEntityDefinition<ScreenFieldDefinition>> partsDefinitions = screenEntitiesRegistry.get(
				screenEntity.getClass()).getPartsDefinitions();
		Set<String> fieldPartNames = partsDefinitions.keySet();
		for (String fieldPartName : fieldPartNames) {
			ScreenPartEntityDefinition screenPartEntityDefinition = (ScreenPartEntityDefinition)partsDefinitions.get(fieldPartName);
			Object partObject = ReflectionUtil.newInstance(screenPartEntityDefinition.getPartClass());
			fieldAccessor.setFieldValue(fieldPartName, partObject);

			SimpleScreenPojoFieldAccessor partFieldAccessor = new SimpleScreenPojoFieldAccessor(partObject);

			Collection<ScreenFieldDefinition> fieldMappingDefinitions = screenPartEntityDefinition.getFieldsDefinitions().values();
			screenBinderLogic.populatedFields(partFieldAccessor, terminalSnapshot, fieldMappingDefinitions);
		}

	}

	public void populateAction(TerminalSendAction sendAction, TerminalSnapshot terminalSnapshot, Object entity) {

		ScreenEntitiesRegistry screenEntitiesRegistry = SpringUtil.getBean(applicationContext, ScreenEntitiesRegistry.class);

		Map<String, PartEntityDefinition<ScreenFieldDefinition>> partsDefinitions = screenEntitiesRegistry.get(entity.getClass()).getPartsDefinitions();

		ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(entity);

		Set<String> fieldPartNames = partsDefinitions.keySet();
		for (String fieldPartName : fieldPartNames) {
			PartEntityDefinition<ScreenFieldDefinition> screenPartEntityDefinition = partsDefinitions.get(fieldPartName);
			Object screenPart = fieldAccessor.getFieldValue(fieldPartName);
			screenBinderLogic.populateSendAction(sendAction, terminalSnapshot, screenPart,
					screenPartEntityDefinition.getFieldsDefinitions().values());
		}

	}

}
