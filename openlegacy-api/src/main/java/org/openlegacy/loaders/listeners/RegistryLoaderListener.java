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

package org.openlegacy.loaders.listeners;

import org.openlegacy.EntitiesRegistry;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author Ivan Bort
 */
public interface RegistryLoaderListener {

	public void registryLoaded(EntitiesRegistry<?, ?, ?> entitiesRegistry, BeanFactory beanFactory);

}
