package org.openlegacy.cache;

import java.io.Serializable;

public class CacheInfo implements Serializable {

	private static final long serialVersionUID = -3126078893255232404L;

	String name;
	long defaultExpiry;
	int elementsCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDefaultExpiry() {
		return defaultExpiry;
	}

	public void setDefaultExpiry(long defaultExpiry) {
		this.defaultExpiry = defaultExpiry;
	}

	public int getElementsCount() {
		return elementsCount;
	}

	public void setElementsCount(int elementsCount) {
		this.elementsCount = elementsCount;
	}

}