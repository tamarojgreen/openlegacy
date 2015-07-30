package org.openlegacy.utils;

import org.openlegacy.types.Base64Array;
import org.openlegacy.types.HexArray;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldUtil {

	protected static final Class<?>[] primitiveTypes = { Byte.class, byte.class, Short.class, short.class, Integer.class,
			int.class, Long.class, long.class, Float.class, float.class, Double.class, double.class, Character.class, char.class,
			Boolean.class, boolean.class, // duplicates must be here
			BigDecimal.class, BigInteger.class, String.class, Date.class, Base64Array.class, HexArray.class, Object.class };

	protected static final int primitiveTypesEndOffset = 7;
	protected static final String VALUE_OF = "valueOf";
	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static final int MAX_ENTRY_VALUE = 999999999;
	private static final int MIN_ENTRY_VALUE = -999999998;

	public static class MinMax {

		private double min = 0, max = 0;

		public double getMin() {
			return min;
		}

		public void setMin(double min) {
			this.min = min < MIN_ENTRY_VALUE ? MIN_ENTRY_VALUE : min;
		}

		public double getMax() {
			return max;
		}

		public void setMax(double max) {
			this.max = max > MAX_ENTRY_VALUE ? MAX_ENTRY_VALUE : max;
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
		if (clazz != null) {
			for (Class<?> primitive : primitiveTypes) {
				if (primitive.getSimpleName().toLowerCase().equals(clazz.toLowerCase())) {
					return getPrimitiveClass(primitive);
				}
			}
		}
		return null;
	}

	public static MinMax getMinMax(Class<?> clazz) {
		MinMax minMax = new MinMax();
		if (clazz != null) {
			if (clazz == String.class) {
				minMax.setMin(0);
				minMax.setMax(255);
			} else {
				try {
					minMax.setMax(Double.valueOf(String.valueOf(clazz.getField("MAX_VALUE").get(null))));
					if (clazz == Double.class || clazz == Float.class) {
						minMax.setMin(MIN_ENTRY_VALUE);
					} else {
						minMax.setMin(Double.valueOf(String.valueOf(clazz.getField("MIN_VALUE").get(null))));
					}
				} catch (Exception e) {
				}
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
			if (clazz == Boolean.class) {
				return 1;
			} else {
				// return clazz != String.class ? (Integer)clazz.getField("BYTES").get(null) : 255;
				return clazz != String.class ? (Integer)clazz.getField("SIZE").get(null) / 8 : 255;
			}
		} catch (Exception e) {
			return 0;
		}
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