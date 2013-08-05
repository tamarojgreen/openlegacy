/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.rpc.support;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcInvokeAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcInvokeAction implements RpcInvokeAction, Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElementWrapper
	@XmlElements({ @XmlElement(name = "field", type = SimpleRpcFlatField.class),
			@XmlElement(name = "structure", type = SimpleRpcStructureField.class) })
	private List<RpcField> rpcFields = new ArrayList<RpcField>();

	@XmlAttribute
	private String action;

	@XmlAttribute
	private String rpcPath;

	public SimpleRpcInvokeAction() {
		// for serialization
	}

	public SimpleRpcInvokeAction(String rpcName) {
		this.rpcPath = rpcName;
	}

	public List<RpcField> getFields() {
		return rpcFields;
	}

	public String getAction() {
		return action;
	}

	public String getRpcPath() {
		return rpcPath;
	}

	public void setRpcPath(String rpcPath) {
		this.rpcPath = rpcPath;
	}

}
