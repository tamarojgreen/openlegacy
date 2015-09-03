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

import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.ServiceParamDetailsDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimpleServiceMethodDefinition implements ServiceMethodDefinition {

	private String name;
	private List<ServiceParamDetailsDefinition> inputParam = new ArrayList<ServiceParamDetailsDefinition>(),
			outputParam = new ArrayList<ServiceParamDetailsDefinition>();
	private long cacheDuration;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ServiceParamDetailsDefinition> getInputParams() {
		return inputParam;
	}

	@Override
	public List<ServiceParamDetailsDefinition> getOutputParams() {
		return outputParam;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInputParams(List<ServiceParamDetailsDefinition> inputParam) {
		this.inputParam = inputParam;
	}

	public void setOutputParams(List<ServiceParamDetailsDefinition> outputParam) {
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
