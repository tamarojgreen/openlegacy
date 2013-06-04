package org.openlegacy.rpc.support;

import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcResult;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcResult implements RpcResult, Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "field", type = SimpleRpcField.class)
	private List<RpcField> rpcFields;

	public List<RpcField> getRpcFields() {
		return rpcFields;
	}

	public void setRpcFields(List<RpcField> rpcFields) {
		this.rpcFields = rpcFields;
	}

}
