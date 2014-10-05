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
package org.openlegacy.utils;

import net.sf.cglib.proxy.Enhancer;

import org.aopalliance.intercept.Interceptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class ProxyUtil {

	public static <T> T getTargetObject(Object proxy) {
		return getTargetObject(proxy, false);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getTargetObject(Object proxy, boolean deep) {
		while (proxy instanceof Advised) {
			try {
				if (deep) {
					// invoke all getters
					PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(proxy);
					for (PropertyDescriptor propertyDescriptor : properties) {
						try {
							Class<?> propertyType = propertyDescriptor.getPropertyType();
							if (propertyType != null && !TypesUtil.isPrimitive(propertyType)) {
								Method readMethod = propertyDescriptor.getReadMethod();
								if (readMethod != null) {
									readMethod.invoke(proxy);
								}
							}
						} catch (Exception e) {
							throw (new RuntimeException(e));
						}
					}
				}
				proxy = ((Advised)proxy).getTargetSource().getTarget();
			} catch (Exception e) {
				throw (new IllegalStateException(e));
			}
		}

		if (deep) {
			DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(proxy);
			PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(proxy);
			for (PropertyDescriptor propertyDescriptor : properties) {
				try {
					Object value = propertyDescriptor.getReadMethod().invoke(proxy);
					Object afterValue = getTargetObject(value, false);
					if (value != afterValue) {
						fieldAccessor.setPropertyValue(propertyDescriptor.getName(), afterValue);
					}
				} catch (Exception e) {
					throw (new RuntimeException(e));
				}
			}
		}
		return (T)proxy;
	}

	public static Class<?> getObjectRealClass(Object object) {
		while (object instanceof TargetClassAware) {
			object = ((TargetClassAware)object).getTargetClass();
		}
		return object.getClass();
	}

	public static Class<?> getOriginalClass(Class<?> entityClass) {
		while (Enhancer.isEnhanced(entityClass)) {
			entityClass = entityClass.getSuperclass();
		}
		return entityClass;
	}

	public static boolean isClassesMatch(Class<?> classA, Class<?> classB) {
		return isClassesMatch(classA, classB, false);
	}

	public static boolean isClassesMatch(Class<?> classA, Class<?> classB, boolean allowNulls) {
		if (allowNulls) {
			if (classA == null || classB == null) {
				return false;
			}
		} else {
			Assert.notNull(classA);
			Assert.notNull(classB);
		}

		classA = getOriginalClass(classA);
		classB = getOriginalClass(classB);

		return (classA == classB);
	}

	public static Object createPojoProxy(Class<?> entityClass, Class<?> entityInterface, Interceptor interceptor) {
		Object entity = ReflectionUtil.newInstance(entityClass);
		ProxyFactory proxyFactory = new ProxyFactory(entityInterface, interceptor);

		proxyFactory.setTarget(entity);
		proxyFactory.setProxyTargetClass(true);
		Object entityProxy = proxyFactory.getProxy();

		return entityProxy;
	}
}
