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
package org.openlegacy.terminal.definitions;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleScreenPartEntityDefinition implements ScreenPartEntityDefinition {

	private Class<?> partClass;

	private final Map<String, ScreenFieldDefinition> fieldDefinitions = new LinkedHashMap<String, ScreenFieldDefinition>();

	private String partName;

	public SimpleScreenPartEntityDefinition(Class<?> partClass) {
		this.partClass = partClass;
	}

	public Class<?> getPartClass() {
		return partClass;
	}

	public Map<String, ScreenFieldDefinition> getFieldsDefinitions() {
		return fieldDefinitions;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

}
