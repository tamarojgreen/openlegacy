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

public class SimpleWebServiceMethodDefinition implements WebServiceMethodDefinition {

	private String name;
	private WebServiceParamDefinition inputParam, outputParam;

	public String getName() {
		return name;
	}

	public WebServiceParamDefinition getInputParam() {
		return inputParam;
	}

	public WebServiceParamDefinition getOutputParam() {
		return outputParam;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInputParam(WebServiceParamDefinition inputParam) {
		this.inputParam = inputParam;
	}

	public void setOutputParam(WebServiceParamDefinition outputParam) {
		this.outputParam = outputParam;
	}

}
