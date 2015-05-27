package org.openlegacy.providers.wsrpc.utils;

import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.xml.soap.SOAPElement;

public class FieldUtil {

	private static final Class<?>[] primitiveTypes = { Byte.class, byte.class, Short.class, short.class, Integer.class,
			int.class, Long.class, long.class, Float.class, float.class, Double.class, Character.class, char.class,
			Boolean.class, boolean.class, // duplicates must be here
			BigDecimal.class, String.class };

	private static final int primitiveTypesEndOffset = 2;
	private static final String VALUE_OF = "valueOf";

	public static boolean isPrimitive(RpcField field) {
		if (field instanceof RpcFlatField) {
			Class<?> clazz = field.getType();
			for (Class<?> primitive : primitiveTypes) {
				if (clazz == primitive) {
					return true;
				}
			}
		}
		return false;
	}

	private static Class<?> getPrimitiveClass(Class<?> clazz) {
		for (int i = 1; i < primitiveTypes.length - primitiveTypesEndOffset; i += 2) {
			if (clazz == primitiveTypes[i]) {
				return primitiveTypes[i - 1];
			}
		}
		return clazz;
	}

	public static void readPrimitiveField(RpcFlatField field, SOAPElement value) throws OpenLegacyException {
		Class<?> clazz = getPrimitiveClass(field.getType());// finds conformity between int and Integer, etc...
		try {
			if (clazz != String.class) {
				Method valueOf = clazz.getMethod(VALUE_OF, String.class);
				field.setValue(valueOf.invoke(null, value.getValue()));
			} else {
				field.setValue(value.getValue());
			}
		} catch (Exception e) {
			throw new OpenLegacyException(String.format("Primitive reader problem on \" %s \" field with value \" %s \"",
					field.getName(), value.getValue()), e);
		}
	}

	public static void writePrimitiveField(RpcFlatField field, SOAPElement out) {
		out.setValue(String.valueOf(field.getValue()));
	}
}
