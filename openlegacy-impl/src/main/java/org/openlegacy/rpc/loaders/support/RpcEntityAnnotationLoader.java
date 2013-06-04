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
package org.openlegacy.rpc.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.rpc.definitions.SimpleRpcEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
@Order(1)
public class RpcEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(RpcEntityAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcEntity.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		RpcEntity rpcAnnotation = (RpcEntity)annotation;
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		String rpcEntityName = rpcAnnotation.name().equals(AnnotationConstants.NULL) ? containingClass.getSimpleName()
				: rpcAnnotation.name();
		String displayName = rpcAnnotation.displayName().equals(AnnotationConstants.NULL) ? StringUtil.toDisplayName(rpcEntityName)
				: rpcAnnotation.displayName();

		SimpleRpcEntityDefinition rpcEntityDefinition = new SimpleRpcEntityDefinition(rpcEntityName, containingClass);
		rpcEntityDefinition.setDisplayName(displayName);
		rpcEntityDefinition.setLanguage(rpcAnnotation.language());

		logger.info(MessageFormat.format("RPC \"{0}\" was added to the RPC registry ({1})", rpcEntityName,
				containingClass.getName()));

		rpcEntitiesRegistry.add(rpcEntityDefinition);
	}

}
