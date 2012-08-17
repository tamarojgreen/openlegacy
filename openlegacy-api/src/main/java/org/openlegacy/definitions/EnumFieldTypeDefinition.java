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
package org.openlegacy.definitions;

import org.openlegacy.DisplayItem;

import java.util.Map;

/**
 * Defines an enum field type registry information stored within {@link FieldDefinition}. An enum field is created from
 * {@link ScreenEnumField} annotation. Send the enum key to the host
 * 
 * @author Roi Mor
 */
public interface EnumFieldTypeDefinition extends FieldTypeDefinition {

	Class<? extends Enum<?>> getEnumClass();

	/**
	 * Map between the field host value to (enum,display)
	 * 
	 * @return map of enums
	 */
	Map<Object, DisplayItem> getEnums();

	/**
	 * Map of (enum,display)
	 * 
	 * @return
	 */
	Map<Object, Object> getDisplayValues();
}
