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

import org.openlegacy.annotations.services.Service;
import org.openlegacy.loaders.support.AbstractcServiceClassAnnotationsLoader;
import org.openlegacy.services.definitions.ServiceDefinition;
import org.openlegacy.services.definitions.SimpleServiceDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Order(1)
public class ServiceAnnotationLoader extends AbstractcServiceClassAnnotationsLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == getAnnotation();
	}

	@Override
	public void load(ServiceDefinition definition, Annotation annotation) {
		Service ann = (Service)annotation;
		if (!(definition instanceof SimpleServiceDefinition)) {
			return;
		}
		SimpleServiceDefinition simpleDefinition = (SimpleServiceDefinition)definition;
		simpleDefinition.setName(ann.name());
	}

	@Override
	public Class<? extends Annotation> getAnnotation() {
		return Service.class;
	}
}
