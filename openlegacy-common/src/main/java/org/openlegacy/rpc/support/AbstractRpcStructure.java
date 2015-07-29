/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.rpc.support;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class AbstractRpcStructure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private int order;

	@XmlAttribute
	private String name = null;

	@XmlAttribute
	private String virtualGroup = "";

	@XmlAttribute
	private String legacyContainerName;

	@XmlTransient
	protected String soapElementName;

	@XmlTransient
	private String listElementName;

	@XmlTransient
	private String[] expandedElements;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getLegacyContainerName() {
		return legacyContainerName;
	}

	public void setLegacyContainerName(String legacyContainerName) {
		this.legacyContainerName = legacyContainerName;
	}

	public String getVirtualGroup() {
		return virtualGroup;
	}

	public void setVirtualGroup(String virtualGroup) {
		this.virtualGroup = virtualGroup;
	}

	public Object getDelegate() {
		return null;
	}

	public String getSoapElementName() {
		return soapElementName;
	}

	public void setSoapElementName(String soapElement) {
		this.soapElementName = soapElement;
	}

	public String getListElementName() {
		return listElementName;
	}

	public void setListElementName(String listElementName) {
		this.listElementName = listElementName;
	}

	public String[] getExpandedElements() {
		return expandedElements;
	}

	public void setExpandedElements(String[] expandedElements) {
		this.expandedElements = expandedElements;
	}

}
