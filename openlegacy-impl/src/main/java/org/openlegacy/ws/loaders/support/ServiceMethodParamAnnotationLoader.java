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

package org.openlegacy.ws.loaders.support;

import org.openlegacy.loaders.support.AbstractWsMethodParamLoader;
import org.openlegacy.ws.definitions.SimpleWebServiceMethodDefinition;
import org.openlegacy.ws.definitions.SimpleWebServiceParamDetailsDefinition;
import org.openlegacy.ws.definitions.WebServiceMethodDefinition;
import org.openlegacy.ws.definitions.WebServiceParamDetailsDefinition;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.jws.WebParam;
import javax.jws.WebResult;

@Component
@Order(3)
public class ServiceMethodParamAnnotationLoader extends AbstractWsMethodParamLoader {

	@Override
	public void load(WebServiceMethodDefinition definition, Method method) {
		if (!(definition instanceof SimpleWebServiceMethodDefinition)) {
			return;
		}
		if (!definition.getInputParams().isEmpty()) {
			int i = 0;
			for (WebServiceParamDetailsDefinition def : definition.getInputParams()) {
				processInputParams((SimpleWebServiceParamDetailsDefinition)def, method, i++);
			}
		}
		if (!definition.getOutputParams().isEmpty()) {
			processOutputParam((SimpleWebServiceParamDetailsDefinition)definition.getOutputParams().get(0), method);
		}
	}

	private void processInputParams(SimpleWebServiceParamDetailsDefinition paramDef, Method method, int paramIndex) {
		WebParam webParam = findMethodParamAnnotation(method.getDeclaringClass(), method, paramIndex, WebParam.class);

		if (webParam == null || webParam.name().trim().equals("")) {
			return;
		} else {
			paramDef.setFieldName(webParam.name());
		}
	}

	private void processOutputParam(SimpleWebServiceParamDetailsDefinition paramDef, Method method) {
		WebResult webResult = AnnotationUtils.findAnnotation(method, WebResult.class);

		if (webResult == null || webResult.name().trim().equals("")) {
			return;
		} else {
			paramDef.setFieldName(webResult.name());
		}
	}

	private <A extends Annotation> A findMethodParamAnnotation(Class<?> methodClazz, Method method, int paramIndex,
			Class<A> annotationClazz) {
		if (methodClazz == null || methodClazz == Object.class) {
			return null;
		}

		Method localMethod = null;
		try {
			localMethod = methodClazz.getMethod(method.getName(), method.getParameterTypes());
		} catch (Exception e) {
			return null;
		}

		A annotation = new MethodParameter(localMethod, paramIndex).getParameterAnnotation(annotationClazz);
		if (annotation != null) {
			return annotation;
		}

		annotation = findMethodParamAnnotation(methodClazz.getSuperclass(), localMethod, paramIndex, annotationClazz);

		if (annotation != null) {
			return annotation;
		}

		if (methodClazz.getInterfaces() != null) {
			for (Class<?> inter : methodClazz.getInterfaces()) {
				annotation = findMethodParamAnnotation(inter, localMethod, paramIndex, annotationClazz);
				if (annotation != null) {
					return annotation;
				}
			}
		}

		return null;
	}
}
