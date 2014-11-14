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

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcResult implements RpcResult, Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElementWrapper
	@XmlElements({ @XmlElement(name = "field", type = SimpleRpcFlatField.class),
			@XmlElement(name = "structure", type = SimpleRpcStructureField.class),
			@XmlElement(name = "structure-list", type = SimpleRpcStructureListField.class) })
	private List<RpcField> rpcFields;

	@Override
	public List<RpcField> getRpcFields() {
		return rpcFields;
	}

	public void setRpcFields(List<RpcField> rpcFields) {
		this.rpcFields = rpcFields;
	}

	@Override
	public RpcResult clone() {
		try {
			SimpleRpcResult c = (SimpleRpcResult)super.clone();
			c.setRpcFields(new ArrayList<RpcField>());
			for (RpcField field : rpcFields) {
				c.getRpcFields().add(field.clone());
			}
			return c;
		} catch (CloneNotSupportedException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

}
