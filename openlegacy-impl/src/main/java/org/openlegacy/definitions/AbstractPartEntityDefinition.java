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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractPartEntityDefinition<F extends FieldDefinition> implements PartEntityDefinition<F>, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<?> partClass;

	private final Map<String, F> fieldsDefinitions = new LinkedHashMap<String, F>();

	private String partName;

	private String displayName;

	public AbstractPartEntityDefinition(Class<?> partClass) {
		this.partClass = partClass;
	}

	public Class<?> getPartClass() {
		return partClass;
	}

	public Map<String, F> getFieldsDefinitions() {
		return fieldsDefinitions;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
