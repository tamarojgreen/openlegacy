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

public class SimpleServiceDefinition implements ServiceDefinition {

	private String name;
	private boolean isRest;
	private List<ServiceMethodDefinition> methods = new ArrayList<ServiceMethodDefinition>();
	private Class<?> webServiceClass;
	private ServicePoolDefinition pool;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ServiceMethodDefinition> getMethods() {
		return methods;
	}

	@Override
	public ServiceMethodDefinition getMethodByName(String name) {
		if (methods != null) {
			for (ServiceMethodDefinition def : methods) {
				if (def.getName().equals(name)) {
					return def;
				}
			}
		}
		return null;
	}

	public void setName(String name) {
		this.name = name;
		PropertyChangedUtils.sendEvent(new PropertyChangedEvent().setBeanOrClassName("serviceRegistry").setEvent(
				PropertyChangedUtils.getEvent(this, "name", name)));
	}

	public void setMethods(List<ServiceMethodDefinition> methods) {
		this.methods = methods;
	}

	public boolean isRest() {
		return isRest;
	}

	public void setRest(boolean isRest) {
		this.isRest = isRest;
	}

	@Override
	public Class<?> getWebServiceClass() {
		return webServiceClass;
	}

	public void setWebServiceClass(Class<?> webServiceClass) {
		this.webServiceClass = webServiceClass;
	}

	@Override
	public ServicePoolDefinition getPool() {
		return pool;
	}

	public void setPool(ServicePoolDefinition webServicePoolDefinition) {
		this.pool = webServicePoolDefinition;
	}

}
