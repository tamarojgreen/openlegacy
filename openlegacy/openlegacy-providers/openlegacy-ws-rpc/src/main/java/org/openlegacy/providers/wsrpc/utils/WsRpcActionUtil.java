package org.openlegacy.providers.wsrpc.utils;

import java.util.Properties;

public class WsRpcActionUtil {

	public static final String TARGET_NAMESPACE = "targetNameSpace";
	public static final String SERVICE_NAME = "serviceName";
	public static final String METHOD_NAME = "methodName";

	public static final String ACTION_FORMAT = "%s%c%s%c%s";

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

	public static WsRpcActionData getWsRpcActionData(Properties props) {

		return new WsRpcActionData().setMethodName(props.getProperty(METHOD_NAME)).setServiceName(props.getProperty(SERVICE_NAME)).setTargetNamespace(
				props.getProperty(TARGET_NAMESPACE));
	}

}
