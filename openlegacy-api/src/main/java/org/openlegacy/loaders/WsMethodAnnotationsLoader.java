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

package org.openlegacy.loaders;

import org.openlegacy.WebServicesRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface WsMethodAnnotationsLoader extends Comparable<WsMethodAnnotationsLoader> {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(WebServicesRegistry registry, Method method, Annotation annotation, Class<?> containingClass);
}
