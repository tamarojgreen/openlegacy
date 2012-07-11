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
package org.openlegacy.terminal.spi;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenPartEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;

/**
 * Define a registry spi for screen related entities registration
 * 
 */
public interface ScreenEntitiesRegistry extends EntitiesRegistry<ScreenEntityDefinition, ScreenFieldDefinition> {

	void addPart(ScreenPartEntityDefinition screenPartEntityDefinition);

	ScreenPartEntityDefinition getPart(Class<?> containingClass);

	void addTable(ScreenTableDefinition tableDefinition);

	ScreenTableDefinition getTable(Class<?> containingClass);

}
