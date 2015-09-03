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

package org.openlegacy.services.loaders.support;

import org.openlegacy.loaders.support.AbstractServiceMethodParamLoader;
import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.ServiceParamDetailsDefinition;
import org.openlegacy.services.definitions.SimpleServiceMethodDefinition;
import org.openlegacy.services.definitions.SimpleServiceParamDetailsDefinition;
import org.openlegacy.utils.FieldUtil;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Order(2)
public class ServiceMethodParamStdNameLoader extends AbstractServiceMethodParamLoader {

	@Override
	public void load(ServiceMethodDefinition definition, Method method) {
		if (!(definition instanceof SimpleServiceMethodDefinition)) {
			return;
		}
		if (!definition.getInputParams().isEmpty()) {
			for (ServiceParamDetailsDefinition def : definition.getInputParams()) {
				if (FieldUtil.isPrimitive(def.getFieldClass())) {
					continue;
				}
				((SimpleServiceParamDetailsDefinition)def).setFieldName(StringUtil.toJavaFieldName(def.getFieldClass().getSimpleName()));
			}
		}
		if (!definition.getOutputParams().isEmpty()) {
			ServiceParamDetailsDefinition def = definition.getOutputParams().get(0);
			((SimpleServiceParamDetailsDefinition)def).setFieldName(StringUtil.toJavaFieldName(def.getFieldClass().getSimpleName()));
		}
	}
}
