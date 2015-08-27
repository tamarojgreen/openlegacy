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

import org.openlegacy.ws.definitions.WebServiceMethodDefinition;

import java.lang.annotation.Annotation;

public interface WsMethodAnnotationsLoader extends Comparable<WsMethodAnnotationsLoader> {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(WebServiceMethodDefinition definition, Annotation annotation);

	Class<? extends Annotation> getAnnotation();
}