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

package org.openlegacy.support;

import org.openlegacy.WebServicesRegistry;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.ClassUtils.FindInClassProcessor;
import org.openlegacy.ws.definitions.WebServiceDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimpleWebServicesRegistry implements WebServicesRegistry {

	private List<WebServiceDefinition> webServices = new ArrayList<WebServiceDefinition>();
	private List<String> packages;

	private FindInClassProcessor process = new FindInClassProcessor() {

		@Override
		public Object process(Class<?> clazz, Object... args) {
			return (clazz == args[0]) ? true : null;
		}
	};

	@Override
	public List<WebServiceDefinition> getWebServices() {
		return webServices;
	}

	@Override
	public WebServiceDefinition getWebServiceByName(String name) {
		if (webServices != null) {
			for (WebServiceDefinition def : webServices) {
				if (def.getName().equals(name)) {
					return def;
				}
			}
		}
		return null;
	}

	public List<String> getPackages() {
		return packages;
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

	public void setWebServices(List<WebServiceDefinition> webServices) {
		this.webServices = webServices;
	}

	@Override
	public WebServiceDefinition getWebServiceByClass(Class<?> clazz) {
		if (webServices != null && clazz != null) {
			for (WebServiceDefinition def : webServices) {
				if (def.getWebServiceClass() == clazz) {
					return def;
				}
			}

			for (WebServiceDefinition def : webServices) {
				if (ClassUtils.findInClass(def.getWebServiceClass(), process, clazz) != null) {
					return def;
				}
			}
		}
		return null;
	}
}
