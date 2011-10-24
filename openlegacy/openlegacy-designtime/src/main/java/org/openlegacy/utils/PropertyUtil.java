package org.openlegacy.utils;

import org.apache.commons.lang.StringUtils;

public class PropertyUtil {

	private static final String GET = "get";
	private static final String SET = "set";

	/**
	 * Return a bean property name from a getter method, if the method is in the format of getXXX
	 * 
	 * @param methodName
	 * @return bean property name of a getter method. null otherwise
	 */
	public static String getPropertyNameIfGetter(String methodName) {
		if (methodName.startsWith(GET)) {
			String noGetMethodName = methodName.substring(GET.length());
			return StringUtils.uncapitalize(noGetMethodName);

		}
		return null;
	}

	/**
	 * Return a bean property name from a setter method, if the method is in the format of setXXX
	 * 
	 * @param methodName
	 * @return bean property name of a setter method. null otherwise
	 */
	public static String getPropertyNameIfSetter(String methodName) {
		if (methodName.startsWith(SET)) {
			String nosetMethodName = methodName.substring(SET.length());
			return StringUtils.uncapitalize(nosetMethodName);

		}
		return null;
	}
}
