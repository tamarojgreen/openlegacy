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
	private List<WebServiceMethodDefinition> methods = new ArrayList<WebServiceMethodDefinition>();

	public String getName() {
		return name;
	}

	public List<WebServiceMethodDefinition> getMethods() {
		return methods;
	}

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

}
