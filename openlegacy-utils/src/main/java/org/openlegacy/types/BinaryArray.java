package org.openlegacy.types;

import java.io.Serializable;
import java.util.Arrays;

public abstract class BinaryArray implements Serializable, Comparable<BinaryArray> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected byte[] value;

	public byte[] getArray() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = null;
		this.value = value.clone();
	}

	@Override
	public int compareTo(BinaryArray paramT) {
		byte[] compare = paramT.getArray();
		if (compare.length == value.length) {
			return 0;
		} else {
			return compare.length > value.length ? -1 : 1;
		}
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else {
			if (obj instanceof BinaryArray) {
				BinaryArray compare = (BinaryArray)obj;
				if (compareTo(compare) != 0) {
					return false;
				} else {
					return Arrays.equals(value, compare.getArray());
				}
			}
		}
		return false;
	}

	public abstract String getValue();

	public abstract void setValue(String value);

	public abstract BinaryArray valueOf(String value);

}
