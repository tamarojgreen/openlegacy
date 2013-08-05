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
package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;

import java.lang.reflect.Field;

/**
 * Field loader serves as the mechanism for translating annotations into {@link EntityDefinition} stored within
 * {@link EntitiesRegistry}. Handles fields without any special annotation, but refers to other entities (e.g screen part, screen
 * table)
 * 
 * @author Roi Mor
 * 
 */
public interface FieldLoader {

	@SuppressWarnings("rawtypes")
	boolean match(EntitiesRegistry entitiesRegistry, Field field);

	@SuppressWarnings("rawtypes")
	void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder);

}
