package org.openlegacy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class ReflectionUtil {

	public static Class<?> getListType(Field field) {
		try {
			ParameterizedType type = (ParameterizedType)field.getGenericType();
			Class<?> listType = (Class<?>)type.getActualTypeArguments()[0];
			return listType;
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

	}
}
