package org.openlegacy.utils;

import net.sf.cglib.proxy.Enhancer;

import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.Advised;

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
		classA = getOriginalClass(classA);
		classB = getOriginalClass(classB);

		return (classA == classB);
	}
}
