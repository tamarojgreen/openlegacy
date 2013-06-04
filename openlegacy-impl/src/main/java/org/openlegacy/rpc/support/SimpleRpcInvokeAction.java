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
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleRpcInvokeAction implements RpcInvokeAction, Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "field", type = SimpleRpcField.class)
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

	public List<RpcField> getRpcFields() {
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
