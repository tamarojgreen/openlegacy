package opr.openlegacy.rpc.wsrpc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.openlegacy.rpc.support.SimpleRpcInvokeAction;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class WsRpcInvokeAction extends SimpleRpcInvokeAction{
	private static final long serialVersionUID = 1L;
	
	@XmlAnyAttribute
	private String namespace;
	
	@XmlAttribute
	private String serviceName;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	

}
