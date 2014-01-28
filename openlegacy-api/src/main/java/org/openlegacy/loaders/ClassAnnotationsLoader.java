/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.loaders;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.EntityDefinition;

import java.lang.annotation.Annotation;

/**
 * Class annotations loader serves as the mechanism for translating annotations into {@link EntityDefinition} stored within
 * {@link EntitiesRegistry}. Handles class level annotations.
 * 
 * @author Roi Mor
 * 
 */
public interface ClassAnnotationsLoader extends Comparable<ClassAnnotationsLoader> {

	boolean match(Annotation annotation);

	@SuppressWarnings("rawtypes")
	void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass);

}
