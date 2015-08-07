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

public class SimpleWebServiceMethodDefinition implements WebServiceMethodDefinition {

	private String name;
	private List<WebServiceParamDetailsDefinition> inputParam = new ArrayList<WebServiceParamDetailsDefinition>(),
			outputParam = new ArrayList<WebServiceParamDetailsDefinition>();
	private long cacheDuration;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<WebServiceParamDetailsDefinition> getInputParams() {
		return inputParam;
	}

	@Override
	public List<WebServiceParamDetailsDefinition> getOutputParams() {
		return outputParam;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInputParams(List<WebServiceParamDetailsDefinition> inputParam) {
		this.inputParam = inputParam;
	}

	public void setOutputParams(List<WebServiceParamDetailsDefinition> outputParam) {
		this.outputParam = outputParam;
	}

	@Override
	public long getCacheDuration() {
		return cacheDuration;
	}

	public void setCacheDuration(long cacheDuration) {
		this.cacheDuration = cacheDuration;
	}

}
