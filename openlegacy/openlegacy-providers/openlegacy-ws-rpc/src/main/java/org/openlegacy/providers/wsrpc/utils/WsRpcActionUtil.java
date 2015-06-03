package org.openlegacy.providers.wsrpc.utils;

import java.util.Map;

import javax.xml.namespace.QName;

public class WsRpcActionUtil {

	public static final String TARGET_NAMESPACE = "targetNameSpace";
	public static final String SERVICE_NAME = "serviceName";
	public static final String METHOD_NAME = "methodName";

	public static final String ACTION_FORMAT = "%s%c%s%c%s";

	public static final String INPUT = "INPUT_PARAMS_GROUP";
	public static final String OUTPUT = "OUTPUT_PARAMS_GROUP";

	public static class WsRpcActionData {

		String targetNamespace, serviceName, methodName;

		public String getTargetNamespace() {
			return targetNamespace;
		}

		public WsRpcActionData setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
			return this;
		}

		public String getServiceName() {
			return serviceName;
		}

		public WsRpcActionData setServiceName(String serviceName) {
			this.serviceName = serviceName;
			return this;
		}

		public String getMethodName() {
			return methodName;
		}

		public WsRpcActionData setMethodName(String methodName) {
			this.methodName = methodName;
			return this;
		}
	}

	public static WsRpcActionData getWsRpcActionData(Map<QName, String> props) {

		return new WsRpcActionData().setMethodName(props.get(new QName(METHOD_NAME))).setServiceName(
				props.get(new QName(SERVICE_NAME))).setTargetNamespace(props.get(new QName(TARGET_NAMESPACE)));
	}

}
