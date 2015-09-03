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

package org.openlegacy.services.loaders.support;

import org.openlegacy.loaders.support.AbstractServiceMethodParamLoader;
import org.openlegacy.services.definitions.ServiceMethodDefinition;
import org.openlegacy.services.definitions.ServiceParamDetailsDefinition;
import org.openlegacy.services.definitions.SimpleServiceMethodDefinition;
import org.openlegacy.services.definitions.SimpleServiceParamDetailsDefinition;
import org.openlegacy.utils.ClassUtils;
import org.openlegacy.utils.ClassUtils.FindInClassProcessor;
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
public class ServiceMethodParamAnnotationLoader extends AbstractServiceMethodParamLoader {

	@Override
	public void load(ServiceMethodDefinition definition, Method method) {
		if (!(definition instanceof SimpleServiceMethodDefinition)) {
			return;
		}
		if (!definition.getInputParams().isEmpty()) {
			int i = 0;
			for (ServiceParamDetailsDefinition def : definition.getInputParams()) {
				processInputParams((SimpleServiceParamDetailsDefinition)def, method, i++);
			}
		}
		if (!definition.getOutputParams().isEmpty()) {
			processOutputParam((SimpleServiceParamDetailsDefinition)definition.getOutputParams().get(0), method);
		}
	}

	private void processInputParams(SimpleServiceParamDetailsDefinition paramDef, Method method, int paramIndex) {
		WebParam webParam = findMethodParamAnnotation(method.getDeclaringClass(), method, paramIndex, WebParam.class);

		if (webParam == null || webParam.name().trim().equals("")) {
			return;
		} else {
			paramDef.setFieldName(webParam.name());
		}
	}

	private void processOutputParam(SimpleServiceParamDetailsDefinition paramDef, Method method) {
		WebResult webResult = AnnotationUtils.findAnnotation(method, WebResult.class);

		if (webResult == null || webResult.name().trim().equals("")) {
			return;
		} else {
			paramDef.setFieldName(webResult.name());
		}
	}

	@SuppressWarnings("unchecked")
	private <A extends Annotation> A findMethodParamAnnotation(Class<?> methodClazz, final Method method, int paramIndex,
			Class<A> annotationClazz) {
		return (A)ClassUtils.findInClass(methodClazz, new FindInClassProcessor() {

			@Override
			public Object process(Class<?> clazz, Object... args) {
				if (clazz == Object.class) {
					return null;
				}

				Method paramMethod = (Method)args[0];
				int paramIndex = (Integer)args[1];
				Class<A> annotationClazz = (Class<A>)args[2];

				Method localMethod;
				try {
					localMethod = clazz.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
				} catch (Exception e) {
					return null;
				}
				A result = new MethodParameter(localMethod, paramIndex).getParameterAnnotation(annotationClazz);
				if (result == null) {
					args[0] = localMethod;
				}
				return result;
			}
		}, method, paramIndex, annotationClazz);

		// if (methodClazz == null || methodClazz == Object.class) {
		// return null;
		// }

		// Method localMethod = null;
		// try {
		// localMethod = methodClazz.getMethod(method.getName(), method.getParameterTypes());
		// } catch (Exception e) {
		// return null;
		// }
		//
		// A annotation = new MethodParameter(localMethod, paramIndex).getParameterAnnotation(annotationClazz);
		// if (annotation != null) {
		// return annotation;
		// }
		//
		// annotation = findMethodParamAnnotation(methodClazz.getSuperclass(), localMethod, paramIndex, annotationClazz);
		//
		// if (annotation != null) {
		// return annotation;
		// }
		//
		// if (methodClazz.getInterfaces() != null) {
		// for (Class<?> inter : methodClazz.getInterfaces()) {
		// annotation = findMethodParamAnnotation(inter, localMethod, paramIndex, annotationClazz);
		// if (annotation != null) {
		// return annotation;
		// }
		// }
		// }
		//
		// return null;
	}
}
