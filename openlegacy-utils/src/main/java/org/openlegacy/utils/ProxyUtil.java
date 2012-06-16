package org.openlegacy.utils;

import net.sf.cglib.proxy.Enhancer;

import org.aopalliance.intercept.Interceptor;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;

public class ProxyUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getTargetObject(Object proxy) {
		while (proxy instanceof Advised) {
			try {
				proxy = ((Advised)proxy).getTargetSource().getTarget();
			} catch (Exception e) {
				throw (new IllegalStateException(e));
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
