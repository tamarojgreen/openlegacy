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

import org.openlegacy.annotations.ws.ServiceMethod;
import org.openlegacy.loaders.support.AbstractWsMethodAnnotationsLoader;
import org.openlegacy.ws.definitions.SimpleWebServiceMethodDefinition;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Order(2)
public class ServiceMethodAnnotationLoader extends AbstractWsMethodAnnotationsLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == getAnnotation();
	}

	@Override
	public void load(WebServiceMethodDefinition definition, Annotation annotation) {
		ServiceMethod ann = (ServiceMethod)annotation;
		if (!(definition instanceof SimpleWebServiceMethodDefinition)) {
			return;
		}
		SimpleWebServiceMethodDefinition simpleDefinition = (SimpleWebServiceMethodDefinition)definition;
		simpleDefinition.setName(ann.name());
		simpleDefinition.setCacheDuration(ann.cacheDuration());
	}

	@Override
	public Class<? extends Annotation> getAnnotation() {
		return ServiceMethod.class;
	}

}
