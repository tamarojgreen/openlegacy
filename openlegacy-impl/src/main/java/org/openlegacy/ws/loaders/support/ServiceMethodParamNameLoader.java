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

package org.openlegacy.ws.loaders.support;

import org.openlegacy.loaders.support.AbstractWsMethodParamLoader;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Order(2)
public class ServiceMethodParamNameLoader extends AbstractWsMethodParamLoader {

	@Override
	public void load(WebServiceMethodDefinition definition, Method method) {

	}

}
