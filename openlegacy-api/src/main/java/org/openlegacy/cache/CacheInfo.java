package org.openlegacy.cache;

import java.io.Serializable;

public class CacheInfo implements Serializable {

	private static final long serialVersionUID = -3126078893255232404L;

	String name;
	String className;
	long defaultExpiry;
	private long currentExpiry;
	int elementsCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public long getDefaultExpiry() {
		return defaultExpiry;
	}

	public void setDefaultExpiry(long defaultExpiry) {
		this.defaultExpiry = defaultExpiry;
	}

	public long getCurrentExpiry() {
		return currentExpiry;
	}

	public void setCurrentExpiry(long currentExpiry) {
		this.currentExpiry = currentExpiry;
	}

	public int getElementsCount() {
		return elementsCount;
	}

	public void setElementsCount(int elementsCount) {
		this.elementsCount = elementsCount;
	}

}