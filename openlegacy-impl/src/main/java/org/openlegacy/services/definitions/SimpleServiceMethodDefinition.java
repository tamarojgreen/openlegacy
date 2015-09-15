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

import org.openlegacy.utils.PropertyChangedUtils;
import org.openlegacy.utils.PropertyChangedUtils.PropertyChangedEvent;

import java.util.ArrayList;
import java.util.List;

public class SimpleServiceMethodDefinition implements ServiceMethodDefinition {

	private String name, serviceName, methodName;
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
		PropertyChangedUtils.sendEvent(new PropertyChangedEvent().setBeanOrClassName("serviceRegistry").setEvent(
				PropertyChangedUtils.getEvent(this, "name", this.name, name)));
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
		PropertyChangedUtils.sendEvent(new PropertyChangedEvent().setBeanOrClassName("serviceRegistry").setEvent(
				PropertyChangedUtils.getEvent(this, "cacheDuration", this.cacheDuration, cacheDuration)));
		this.cacheDuration = cacheDuration;
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}
