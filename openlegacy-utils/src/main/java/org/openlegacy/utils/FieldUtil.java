package org.openlegacy.utils;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FieldUtil {

	protected static final Class<?>[] primitiveTypes = { Byte.class, byte.class, Short.class, short.class, Integer.class,
			int.class, Long.class, long.class, Float.class, float.class, Double.class, double.class, Character.class, char.class,
			Boolean.class, boolean.class, // duplicates must be here
			BigDecimal.class, BigInteger.class, String.class };

	protected static final int primitiveTypesEndOffset = 3;
	protected static final String VALUE_OF = "valueOf";

	public static class MinMax {

		private double min = 0, max = 0;

		public double getMin() {
			return min;
		}

		public void setMin(double min) {
			this.min = min;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max;
		}
	}

	public static boolean isPrimitive(Class<?> clazz) {
		for (Class<?> primitive : primitiveTypes) {
			if (clazz == primitive) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPrimitive(String clazz) {
		return clazz == null ? false : getPrimitiveClass(clazz) != null;
	}

	protected static Class<?> getPrimitiveClass(Class<?> clazz) {
		for (int i = 1; i < primitiveTypes.length - primitiveTypesEndOffset; i += 2) {
			if (clazz == primitiveTypes[i]) {
				return primitiveTypes[i - 1];
			}
		}
		return clazz;
	}

	public static Class<?> getPrimitiveClass(String clazz) {
		for (Class<?> primitive : primitiveTypes) {
			if (primitive.getSimpleName().toLowerCase().equals(clazz.toLowerCase())) {
				return getPrimitiveClass(primitive);
			}
		}
		return null;
	}

	public static MinMax getMinMax(Class<?> clazz) {
		if (clazz == null)
			throw (new OpenLegacyRuntimeException(new Exception("Unknown class")));

		MinMax minMax = new MinMax();

		if (clazz == String.class) {
			minMax.setMin(0);
			minMax.setMax(255);
		} else {
			try {
				minMax.setMin(Double.valueOf(String.valueOf(clazz.getField("MIN_VALUE").get(null))));
				minMax.setMax(Double.valueOf(String.valueOf(clazz.getField("MAX_VALUE").get(null))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return minMax;
	}

	public static MinMax getMinMax(String clazz) {
		return getMinMax(getPrimitiveClass(clazz));
	}

	public static int getSize(String clazz) {
		return getSize(getPrimitiveClass(clazz));
	}

	public static int getSize(Class<?> clazz) {
		try {
			return (Integer)clazz.getField("BYTES").get(null);
		} catch (Exception e) {
		}
		return 0;
	}

	public static int getMantissa(String clazz) {
		return getMantissa(getPrimitiveClass(clazz));
	}

	public static int getMantissa(Class<?> clazz) {
		if (clazz == Float.class || clazz == Double.class) {
			try {
				int size = getSize(clazz) * 4;
				int exp = (Integer)clazz.getField("MAX_EXPONENT").get(null);
				int expBits = (int)(Math.log(exp + 1) / Math.log(2)) + 1;
				return (int)((size - expBits) * Math.log(2) / Math.log(10));
			} catch (Exception e) {
			}
		}
		return 0;
	}
}