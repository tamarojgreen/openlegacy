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
package org.openlegacy.definitions.support;

import org.openlegacy.DisplayItem;
import org.openlegacy.definitions.EnumFieldTypeDefinition;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class SimpleEnumFieldTypeDefinition implements EnumFieldTypeDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<? extends Enum<?>> enumClass;
	private Map<Object, DisplayItem> map = new TreeMap<Object, DisplayItem>();
	private TreeMap<Object, Object> displayValues;

	public String getTypeName() {
		return "enum";
	}

	public Class<? extends Enum<?>> getEnumClass() {
		return enumClass;
	}

	public void setEnumClass(Class<? extends Enum<?>> enumClass) {
		this.enumClass = enumClass;
	}

	public Map<Object, DisplayItem> getEnums() {
		return map;
	}

	/**
	 * Comfort method for display purposes
	 */
	public Map<Object, Object> getDisplayValues() {
		if (displayValues != null) {
			return displayValues;
		}

		Collection<DisplayItem> displayItems = map.values();
		displayValues = new TreeMap<Object, Object>();
		for (DisplayItem displayItem : displayItems) {
			displayValues.put(displayItem.getValue(), displayItem.getDisplay());
		}
		return displayValues;
	}
}
