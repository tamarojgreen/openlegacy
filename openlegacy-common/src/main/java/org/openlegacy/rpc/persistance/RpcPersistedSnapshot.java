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
package org.openlegacy.rpc.persistance;

import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class RpcPersistedSnapshot implements RpcSnapshot {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "invoke", type = SimpleRpcInvokeAction.class)
	private RpcInvokeAction rpcInvokeAction;

	@XmlElement(name = "result", type = SimpleRpcResult.class)
	private RpcResult rpcResult;

	@XmlAttribute
	private Integer sequence = null;

	@XmlAttribute
	String entityName;

	public RpcPersistedSnapshot() {
		// for serialization purposes
	}

	public RpcPersistedSnapshot(RpcInvokeAction rpInvokeAction, RpcResult rpcResult, Integer sequence, String entityName) {
		this.rpcInvokeAction = rpInvokeAction;
		this.rpcResult = rpcResult;
		this.sequence = sequence;
		this.entityName = entityName;
	}

	@Override
	public RpcInvokeAction getRpcInvokeAction() {
		return rpcInvokeAction;
	}

	public void setRpcInvokeAction(RpcInvokeAction rpcInvokeAction) {
		this.rpcInvokeAction = rpcInvokeAction;
	}

	@Override
	public RpcResult getRpcResult() {
		return rpcResult;
	}

	public void setRpcResult(RpcResult rpcResult) {
		this.rpcResult = rpcResult;
	}

	@Override
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public String getEntityName() {

		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
