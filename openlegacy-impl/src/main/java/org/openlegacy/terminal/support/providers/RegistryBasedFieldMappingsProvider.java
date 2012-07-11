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
package org.openlegacy.terminal.support.providers;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.providers.ScreenFieldsDefinitionProvider;
import org.openlegacy.terminal.support.DefaultScreenEntitiesRegistry;

import java.util.Collection;

import javax.inject.Inject;

/**
 * A FieldMappingsDefinitionProvider based on open legacy @FieldMapping annotation kept in ScreenEntitiesRegistry
 * 
 */
public class RegistryBasedFieldMappingsProvider implements ScreenFieldsDefinitionProvider {

	@Inject
	private DefaultScreenEntitiesRegistry screenEntitiesRegistry;

	public Collection<ScreenFieldDefinition> getFieldsMappingDefinitions(TerminalSnapshot terminalSnapshot, Class<?> screenEntity) {
		ScreenEntityDefinition screenEntityDefinition = screenEntitiesRegistry.get(screenEntity);
		return screenEntityDefinition.getFieldsDefinitions().values();
	}
}
