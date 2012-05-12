package org.openlegacy.web.tags;

public class TagUtils {

	public static boolean isAssignable(Class<?> actualClass, String expectedClassName) {
		Class<?> expectedClass;
		try {
			expectedClass = Class.forName(expectedClassName);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return expectedClass.isAssignableFrom(actualClass);
	}

	public static boolean instanceOf(Object o, String className) {
		boolean returnValue;

		try {
			returnValue = Class.forName(className).isInstance(o);
		}

		catch (ClassNotFoundException e) {
			returnValue = false;
		}

		return returnValue;
	}

}