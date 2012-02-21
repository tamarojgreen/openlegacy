package org.openlegacy.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtil {

	/**
	 * Utility class which relies on project conventions. When the provided class is an implementation class, then return it from
	 * spring context. If not, return bean named: "default"+className
	 * 
	 * @param applicationContext
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDefaultBean(ApplicationContext applicationContext, Class<T> clazz) {
		if (!clazz.isInterface()) {
			return applicationContext.getBean(clazz);
		}
		return (T)applicationContext.getBean("default" + clazz.getSimpleName());

	}
}
