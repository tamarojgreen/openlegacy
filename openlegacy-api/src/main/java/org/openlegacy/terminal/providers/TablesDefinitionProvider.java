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

import org.openlegacy.terminal.definitions.ScreenTableDefinition;

import java.util.Map;

/**
 * Table definitions provider purpose is to return table definitions for a given screen entity.
 * 
 * @author Roi Mor
 */
public interface TablesDefinitionProvider extends DefinitionsProvider {

	Map<String, ScreenTableDefinition> getTableDefinitions(Class<?> screenEntityClass);

}
