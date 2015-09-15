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

import org.openlegacy.annotations.services.ServiceMethod;
import org.openlegacy.loaders.support.AbstractServiceMethodAnnotationsLoader;
import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.SimpleServiceMethodDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
@Order(2)
public class ServiceMethodAnnotationLoader extends AbstractServiceMethodAnnotationsLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == getAnnotation();
	}

	@Override
	public void load(ServiceMethodDefinition definition, Method method) {
		ServiceMethod ann = (ServiceMethod)AnnotationUtils.findAnnotation(method, getAnnotation());
		if (!(definition instanceof SimpleServiceMethodDefinition)) {
			return;
		}
		SimpleServiceMethodDefinition simpleDefinition = (SimpleServiceMethodDefinition)definition;
		simpleDefinition.setName(ann.name());
		simpleDefinition.setCacheDuration(ann.cacheDuration());
		simpleDefinition.setMethodName(method.getName());
	}

	@Override
	public Class<? extends Annotation> getAnnotation() {
		return ServiceMethod.class;
	}

}
