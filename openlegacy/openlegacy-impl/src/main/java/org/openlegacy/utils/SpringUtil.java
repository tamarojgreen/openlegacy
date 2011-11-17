package org.openlegacy.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getDefaultBean(ApplicationContext applicationContext, Class<T> clazz) {
		if (!clazz.isInterface()) {
			return applicationContext.getBean(clazz);
		}
		return (T)applicationContext.getBean("default" + clazz.getSimpleName());

	}
}
