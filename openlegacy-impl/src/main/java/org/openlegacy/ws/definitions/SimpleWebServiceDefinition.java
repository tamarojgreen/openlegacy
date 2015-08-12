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

public class SimpleWebServiceDefinition implements WebServiceDefinition {

	private String name;
	private boolean isRest;
	private List<WebServiceMethodDefinition> methods = new ArrayList<WebServiceMethodDefinition>();
	private Class<?> webServiceClass;
	private WebServicePoolDefinition pool;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<WebServiceMethodDefinition> getMethods() {
		return methods;
	}

	@Override
	public WebServiceMethodDefinition getMethodByName(String name) {
		if (methods != null) {
			for (WebServiceMethodDefinition def : methods) {
				if (def.getName().equals(name)) {
					return def;
				}
			}
		}
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMethods(List<WebServiceMethodDefinition> methods) {
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
	public WebServicePoolDefinition getPool() {
		return pool;
	}

	public void setPool(WebServicePoolDefinition webServicePoolDefinition) {
		this.pool = webServicePoolDefinition;
	}

}
