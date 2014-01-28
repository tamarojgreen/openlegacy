/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.definitions;

import java.util.Map;

/**
 * An entity part definition defines a repeatable class with mappings which can belongs to a 1 or more entities.
 * 
 * @author Roi Mor
 */
public interface PartEntityDefinition<F extends FieldDefinition> {

	Class<?> getPartClass();

	Map<String, F> getFieldsDefinitions();

	String getDisplayName();

	String getPartName();

}
