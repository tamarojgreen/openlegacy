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

import javax.xml.bind.annotation.XmlTransient;

public abstract class AbstractRpcStructure {

	@XmlTransient
	private int order;

	// @XmlAttribute
	private String name = null;

	private String virtualGroup = "";

	private String legacyContainerName;

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
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

}
