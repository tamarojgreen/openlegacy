package org.openlegacy.rpc.support;

import javax.xml.bind.annotation.XmlTransient;

public abstract class AbstractRpcStructure {

	@XmlTransient
	private int order;

	// @XmlAttribute
	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Object getDelegate() {
		return null;
	}

}
