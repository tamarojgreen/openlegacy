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
package org.openlegacy.rpc.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.rpc.RpcNavigation;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcNavigationDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
public class RpcNavigationAnnotationLoader extends AbstractClassAnnotationLoader {

	// private final static Log logger = LogFactory.getLog(RpcNavigationAnnotationLoader.class);

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcNavigation.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		RpcEntityDefinition rpcEntityDefinition = (RpcEntityDefinition)entitiesRegistry.get(containingClass);
		Assert.notNull(rpcEntityDefinition, MessageFormat.format(
				"RPC entity definition for class {0} not found. Verify @RpcNavigation is defined along @RpcEntity annotation",
				containingClass.getName()));

		RpcNavigation rpcNavigation = (RpcNavigation)annotation;

		SimpleRpcNavigationDefinition rpcNavigationDefinition = (SimpleRpcNavigationDefinition)rpcEntityDefinition.getNavigationDefinition();
		rpcNavigationDefinition.setCategory(rpcNavigation.category());
	}
}
