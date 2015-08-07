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
import org.openlegacy.utils.FieldUtil;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.ws.definitions.SimpleWebServiceMethodDefinition;
import org.openlegacy.ws.definitions.SimpleWebServiceParamDetailsDefinition;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;
import org.openlegacy.ws.definitions.WebServiceParamDetailsDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Order(2)
public class ServiceMethodParamStdNameLoader extends AbstractWsMethodParamLoader {

	@Override
	public void load(WebServiceMethodDefinition definition, Method method) {
		if (!(definition instanceof SimpleWebServiceMethodDefinition)) {
			return;
		}
		if (!definition.getInputParams().isEmpty()) {
			for (WebServiceParamDetailsDefinition def : definition.getInputParams()) {
				if (FieldUtil.isPrimitive(def.getFieldClass())) {
					continue;
				}
				((SimpleWebServiceParamDetailsDefinition)def).setFieldName(StringUtil.toJavaFieldName(def.getFieldClass().getSimpleName()));
			}
		}
		if (!definition.getOutputParams().isEmpty()) {
			WebServiceParamDetailsDefinition def = definition.getOutputParams().get(0);
			((SimpleWebServiceParamDetailsDefinition)def).setFieldName(StringUtil.toJavaFieldName(def.getFieldClass().getSimpleName()));
		}
	}
}
