/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
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

import java.util.Collection;

public interface RegistryLoader {

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Collection<ClassAnnotationsLoader> annotationLoaders,
			Collection<FieldAnnotationsLoader> fieldAnnotationLoaders, Collection<FieldLoader> fieldLoaders);
}
