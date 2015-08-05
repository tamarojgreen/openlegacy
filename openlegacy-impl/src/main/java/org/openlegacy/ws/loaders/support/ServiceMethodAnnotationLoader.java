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

import org.openlegacy.WebServicesRegistry;
import org.openlegacy.loaders.support.AbstractWsMethodAnnotationsLoader;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
@Order(2)
public class ServiceMethodAnnotationLoader extends AbstractWsMethodAnnotationsLoader {

	@Override
	public boolean match(Annotation annotation) {
		return false;
	}

	@Override
	public void load(WebServicesRegistry registry, Method method, Annotation annotation, Class<?> containingClass) {

	}

}
