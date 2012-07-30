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
package org.openlegacy.terminal.providers;

import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;

import java.util.Collection;

/**
 * Field mapping provider purpose is to return mappings for a given screen entity. Implementation depends on the legacy provider.
 * 
 * @author Roi Mor
 */
public interface ScreenFieldsDefinitionProvider extends DefinitionsProvider {

	Collection<ScreenFieldDefinition> getFieldsMappingDefinitions(TerminalSnapshot terminalSnapshot, Class<?> screenEntityClass);

}
