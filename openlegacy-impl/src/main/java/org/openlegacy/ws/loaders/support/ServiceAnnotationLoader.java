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

import org.openlegacy.annotations.ws.Service;
import org.openlegacy.loaders.support.AbstractWsClassAnnotationsLoader;
import org.openlegacy.ws.definitions.SimpleWebServiceDefinition;
import org.openlegacy.ws.definitions.WebServiceDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Order(1)
public class ServiceAnnotationLoader extends AbstractWsClassAnnotationsLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == getAnnotation();
	}

	@Override
	public void load(WebServiceDefinition definition, Annotation annotation) {
		Service ann = (Service)annotation;
		if (!(definition instanceof SimpleWebServiceDefinition)) {
			return;
		}
		SimpleWebServiceDefinition simpleDefinition = (SimpleWebServiceDefinition)definition;
		simpleDefinition.setName(ann.name());
	}

	@Override
	public Class<? extends Annotation> getAnnotation() {
		return Service.class;
	}
}
