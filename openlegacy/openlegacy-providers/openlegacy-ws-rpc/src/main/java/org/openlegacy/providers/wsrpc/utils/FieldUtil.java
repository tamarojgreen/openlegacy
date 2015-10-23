package org.openlegacy.providers.wsrpc.utils;

import org.apache.commons.lang.ClassUtils;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.types.BinaryArray;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.soap.SOAPElement;

public class FieldUtil extends org.openlegacy.utils.FieldUtil {

	public static void readPrimitiveField(RpcFlatField field, SOAPElement value, Object... values) throws OpenLegacyException {
		Class<?> clazz = getPrimitiveClass(field.getType());// finds conformity between int and Integer, etc...
		try {
			if (clazz == Date.class) {
				field.setValue(value.getValue());
			} else if (ClassUtils.getAllSuperclasses(field.getType()).contains(BinaryArray.class)) {
				BinaryArray arr = (BinaryArray)field.getType().newInstance();
				arr.setValue(value.getValue());
				field.setValue(arr);
			} else if (clazz == BigDecimal.class) {
				// http://stackoverflow.com/a/3752626
				field.setValue(new BigDecimal(value.getValue().replaceAll(",", "")));
			} else if (clazz != String.class) {
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
		if (ClassUtils.getAllSuperclasses(field.getValue().getClass()).contains(BinaryArray.class)) {
			out.setValue(((BinaryArray)field.getValue()).getValue());
		} else {
			out.setValue(String.valueOf(field.getValue()));
		}
	}

	public static boolean isPrimitive(RpcField field) {
		if (field instanceof RpcFlatField) {
			return isPrimitive(field.getType());
		}
		return false;
	}
}