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

import java.util.Map;
import java.util.TreeMap;

public class SimpleEnumFieldTypeDefinition implements EnumFieldTypeDefinition {

	private Class<? extends Enum<?>> enumClass;
	private Map<Object, DisplayItem> map = new TreeMap<Object, DisplayItem>();

	public String getTypeName() {
		return "enum";
	}

	public Class<? extends Enum<?>> getEnum() {
		return enumClass;
	}

	public void setEnum(Class<? extends Enum<?>> enumClass) {
		this.enumClass = enumClass;
	}

	public Map<Object, DisplayItem> getAsMap() {
		return map;
	}

}
