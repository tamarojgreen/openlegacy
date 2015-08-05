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

package org.openlegacy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.WebServicesRegistry;
import org.openlegacy.loaders.WebServicesRegistryLoader;
import org.openlegacy.loaders.WsClassAnnotationsLoader;
import org.openlegacy.loaders.WsMethodAnnotationsLoader;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

public class DefaultWebServicesRegistryLoader implements WebServicesRegistryLoader {

	private final static Log logger = LogFactory.getLog(DefaultWebServicesRegistryLoader.class);

	private Collection<WsClassAnnotationsLoader> classAnnotationsLoaders;
	private Collection<WsMethodAnnotationsLoader> methodAnnotationsLoaders;

	@Override
	public void load(WebServicesRegistry registry) {
		Assert.notNull(classAnnotationsLoaders);
		Assert.notNull(methodAnnotationsLoaders);

		List<String> packages = registry.getPackages();
		if (packages == null) {
			logger.warn("Not packages defined for entity registry " + registry.getClass());
			return;
		}
	}

	public void setClassAnnotationsLoaders(Collection<WsClassAnnotationsLoader> classAnnotationsLoaders) {
		this.classAnnotationsLoaders = classAnnotationsLoaders;
	}

	public void setMethodAnnotationsLoaders(Collection<WsMethodAnnotationsLoader> methodAnnotationsLoaders) {
		this.methodAnnotationsLoaders = methodAnnotationsLoaders;
	}

}
