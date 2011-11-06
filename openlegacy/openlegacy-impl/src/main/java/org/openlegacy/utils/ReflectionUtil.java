package org.openlegacy.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

public class ReflectionUtil {

	public static Class<?> getListType(Class<?> containingClass, String fieldName) {
		try {
			Field field = containingClass.getDeclaredField(fieldName);

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
			throw (new IllegalArgumentException(e));
		}

	}

	public static <T> T newInstance(Class<? extends T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw (new IllegalArgumentException(e));
		} catch (IllegalAccessException e) {
			throw (new IllegalArgumentException(e));
		}
	}

	public static void copyProperties(Object destination, Object source) {
		try {
			BeanUtils.copyProperties(destination, source);
		} catch (IllegalAccessException e) {
			throw (new IllegalArgumentException(e));
		} catch (InvocationTargetException e) {
			throw (new IllegalArgumentException(e));
		}
	}
}
