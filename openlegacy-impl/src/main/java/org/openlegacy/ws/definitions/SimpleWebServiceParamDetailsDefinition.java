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

package org.openlegacy.ws.definitions;

import java.util.ArrayList;
import java.util.List;

public class SimpleWebServiceParamDetailsDefinition implements WebServiceParamDetailsDefinition {

	private String fieldName;
	private Class<?> fieldClass;
	private List<WebServiceParamDetailsDefinition> fields = new ArrayList<WebServiceParamDetailsDefinition>();

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public Class<?> getFieldClass() {
		return fieldClass;
	}

	@Override
	public List<WebServiceParamDetailsDefinition> getFields() {
		return fields;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setFieldClass(Class<?> fieldClass) {
		this.fieldClass = fieldClass;
	}

	public void setFields(List<WebServiceParamDetailsDefinition> fields) {
		this.fields = fields;
	}

}
