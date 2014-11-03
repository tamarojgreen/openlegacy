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
import org.openlegacy.annotations.rpc.RpcPart;
import org.openlegacy.annotations.screen.AnnotationConstants;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.rpc.definitions.SimpleRpcPartEntityDefinition;
import org.openlegacy.rpc.services.RpcEntitiesRegistry;
import org.openlegacy.utils.StringUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@Order(2)
public class RpcPartEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	@Override
	public boolean match(Annotation annotation) {
		return annotation.annotationType() == RpcPart.class;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		RpcPart rpcPartAnnotation = (RpcPart)annotation;
		RpcEntitiesRegistry rpcEntitiesRegistry = (RpcEntitiesRegistry)entitiesRegistry;

		SimpleRpcPartEntityDefinition rpcPartEntityDefinition = new SimpleRpcPartEntityDefinition(containingClass);
		String name = StringUtil.toJavaFieldName(containingClass.getSimpleName());
		rpcPartEntityDefinition.setPartName(name);
		if (!rpcPartAnnotation.legacyContainerName().equals(AnnotationConstants.NULL)) {
			rpcPartEntityDefinition.setLegacyContainerName(rpcPartAnnotation.legacyContainerName());
		}
		if (!rpcPartAnnotation.helpText().equals(AnnotationConstants.NULL)) {
			rpcPartEntityDefinition.setHelpText(rpcPartAnnotation.helpText());
		}
		String displayName = null;
		if (rpcPartAnnotation.displayName().equals(AnnotationConstants.NULL)) {
			displayName = StringUtil.toDisplayName(name);
		} else if (rpcPartAnnotation.displayName().length() > 0) {
			displayName = rpcPartAnnotation.displayName();
		}

		rpcPartEntityDefinition.setDisplayName(displayName);
		rpcPartEntityDefinition.setVirtual(rpcPartAnnotation.virtual());

		// convert part name to full
		String partClassName = rpcPartEntityDefinition.getPartClass().toString();

		rpcPartEntityDefinition.setPartFullName(partClassName.substring(partClassName.indexOf("$") + 1).replace('$', '.'));
		rpcEntitiesRegistry.addPart(rpcPartEntityDefinition);
	}
}
