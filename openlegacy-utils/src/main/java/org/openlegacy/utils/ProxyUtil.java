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
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class ProxyUtil {

	public static <T> T getTargetObject(Object proxy) {
		return getTargetObject(proxy, false);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getTargetObject(Object proxy, boolean deep) {
		while (proxy instanceof Advised) {
			try {
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

	@SuppressWarnings("unchecked")
	public static <T> T getTargetJpaObject(Object proxy, boolean children) {
		try {
			Map<Object, Object> processed = new HashMap<Object, Object>();
			Dehibernator dehibernator = new Dehibernator();
			proxy = getTargetJpaObject(proxy, children, dehibernator, processed);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return (T)proxy;
	}

	private static <T> T getTargetJpaObject(Object proxy, boolean children, Dehibernator dehibernator,
			Map<Object, Object> processed) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (proxy == null || processed.containsKey(proxy.getClass().getSimpleName())) {
			return null;
		}

		if (proxy instanceof Collection) {
			for (Object object : (Collection)proxy) {
				object = fetchLazyObject(object, dehibernator, children, processed);
			}
		} else if (proxy instanceof Map) {
			Collection collection = ((Map)proxy).values();
			for (Object object : collection) {
				object = fetchLazyObject(object, dehibernator, children, processed);
			}
		} else {
			proxy = fetchLazyObject(proxy, dehibernator, children, processed);
		}

		return (T)proxy;
	}

	private static <T> T fetchLazyObject(Object proxy, Dehibernator dehibernator, boolean children, Map<Object, Object> processed)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Field[] declaredFields = proxy.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			Class<?> type = field.getType();

			if (type.isAssignableFrom(List.class)) {
				processCollectionField(field, proxy, dehibernator, children, processed);
			} else if (type.isAssignableFrom(Map.class)) {
				processCollectionField(field, proxy, dehibernator, children, processed);
			} else if (processed.containsKey(type.getSimpleName())) {
				setFieldValue(field, proxy, null);
			}

		}
		return (T)proxy;
	}

	private static void processCollectionField(Field field, Object proxy, Dehibernator dehibernator, boolean children,
			Map<Object, Object> processed) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		OneToMany otmAnnotation = field.getAnnotation(OneToMany.class);
		ManyToOne mtoAnnotation = field.getAnnotation(ManyToOne.class);

		if (otmAnnotation != null || mtoAnnotation != null) {
			if (children || (otmAnnotation != null && otmAnnotation.fetch().equals(FetchType.EAGER))
					|| (mtoAnnotation != null && mtoAnnotation.fetch().equals(FetchType.EAGER))) {
				for (Method method : proxy.getClass().getMethods()) {
					if (method.getName().startsWith("get" + StringUtils.capitalize(field.getName()))) {
						Object res = method.invoke(proxy, new Object[0]);
						res.toString();
						res = dehibernator.clean(res);
						processed.put(proxy.getClass().getSimpleName(), proxy);
						setFieldValue(field, proxy, res);
						getTargetJpaObject(res, children, dehibernator, processed);
					}
				}
			} else {
				setFieldValue(field, proxy, null);
			}
		}
	}

	private static void setFieldValue(Field field, Object proxy, Object value) throws IllegalAccessException,
			IllegalArgumentException {
		field.setAccessible(true);
		field.set(proxy, value);

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
