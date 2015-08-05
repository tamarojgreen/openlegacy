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

public class SimpleWebServiceParamDefinition implements WebServiceParamDefinition {

	private WebServiceParamType webServiceParamType;
	private WebServiceParamDetailsDefinition paramDetails;

	@Override
	public WebServiceParamType getWebServiceParamType() {
		return webServiceParamType;
	}

	@Override
	public WebServiceParamDetailsDefinition getParamDetails() {
		return paramDetails;
	}

	public void setWebServiceParamType(WebServiceParamType webServiceParamType) {
		this.webServiceParamType = webServiceParamType;
	}

	public void setParamDetails(WebServiceParamDetailsDefinition paramDetails) {
		this.paramDetails = paramDetails;
	}

}
