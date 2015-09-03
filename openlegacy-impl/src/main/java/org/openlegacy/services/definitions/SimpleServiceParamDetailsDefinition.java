/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.services.definitions;

import org.openlegacy.services.definitions.ServiceParamDetailsDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimpleServiceParamDetailsDefinition implements ServiceParamDetailsDefinition {

	private String fieldName;
	private Class<?> fieldClass;
	private List<ServiceParamDetailsDefinition> fields = new ArrayList<ServiceParamDetailsDefinition>();

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public Class<?> getFieldClass() {
		return fieldClass;
	}

	@Override
	public List<ServiceParamDetailsDefinition> getFields() {
		return fields;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setFieldClass(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}

	public void setFields(List<ServiceParamDetailsDefinition> fields) {
		this.fields = fields;
	}

}
