package org.openlegacy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class ReflectionUtil {

	public static Class<?> getListType(Field field) {
		try {
			if (!(field.getGenericType() instanceof ParameterizedType)) {
				return null;
			}

			ParameterizedType type = (ParameterizedType)field.getGenericType();
			if (type.getActualTypeArguments().length == 0) {
				return null;
			}
			Class<?> listType = (Class<?>)type.getActualTypeArguments()[0];
			return listType;
		} catch (Exception e) {
			throw (new IllegalStateException(e));
		}

	}
}
