package org.openlegacy.providers.wsrpc.utils;

public class WsRpcActionUtil {

	public static final char SEPARATOR = (char)30; // be careful of it, eclipse debugger doesn`t shows untyped symbol of string!
													// But string contains it)

	public static final int TARGET_NAMESPACE = 0;
	public static final int SERVICE_NAME = 1;
	public static final int METHOD_NAME = 2;
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

	public static String buildWsRpcAction(String targetNamespace, String serviceName, String methodName) {
		return String.format(ACTION_FORMAT, targetNamespace, SEPARATOR, serviceName, SEPARATOR, methodName);
	}

	public static String buildWsRpcAction(WsRpcActionData data) {
		return String.format(ACTION_FORMAT, data.getTargetNamespace(), SEPARATOR, data.getServiceName(), SEPARATOR,
				data.getMethodName());
	}

	public static WsRpcActionData getWsRpcActionData(String action) {
		String[] temp = action.split(String.valueOf(SEPARATOR));
		return new WsRpcActionData().setTargetNamespace(temp[TARGET_NAMESPACE]).setServiceName(temp[SERVICE_NAME]).setMethodName(
				temp[METHOD_NAME]);
	}

}
