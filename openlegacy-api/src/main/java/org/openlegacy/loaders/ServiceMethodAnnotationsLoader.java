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

import org.openlegacy.services.definitions.ServiceMethodDefinition;

import java.lang.annotation.Annotation;

public interface ServiceMethodAnnotationsLoader extends Comparable<ServiceMethodAnnotationsLoader> {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(ServiceMethodDefinition definition, Annotation annotation);

	Class<? extends Annotation> getAnnotation();
}
