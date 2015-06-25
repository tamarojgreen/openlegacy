package org.openlegacy.utils;

import java.util.Map;

import javax.xml.namespace.QName;

public class WsRpcActionUtil {

	public static final String TARGET_NAMESPACE = "targetNameSpace";
	public static final String SERVICE_NAME = "serviceName";
	public static final String METHOD_INPUT_NAME = "methodInputName";
	public static final String METHOD_OUTPUT_NAME = "methodOutputName";

	public static final String METHOD_INPUT_NAMESPACE = "methodInputNamespace";
	public static final String METHOD_OUTPUT_NAMESPACE = "methodOutputNamespace";

	public static final String INPUT = "OperationInputParams";
	public static final String OUTPUT = "OperationOutputParams";
	public static final String INPUT_OUTPUT = "OperationInputOutputParams";

	public static final String RPC_PART_INPUT = "rpcPartInput";
	public static final String RPC_PART_OUTPUT = "rpcPartOutput";

	public static class WsRpcActionData {

		String serviceName, methodInputName, methodOutputName, methodInputNameSpace, methodOutputNameSpace, rpcPartInput,
				rpcPartOutput;

		public String getServiceName() {
			return serviceName;
		}

		public WsRpcActionData setServiceName(String serviceName) {
			this.serviceName = serviceName;
			return this;
		}

		public String getMethodInputName() {
			return methodInputName;
		}

		public WsRpcActionData setMethodInputName(String methodInputName) {
			this.methodInputName = methodInputName;
			return this;
		}

		public String getMethodOutputName() {
			return methodOutputName;
		}

		public WsRpcActionData setMethodOutputName(String methodOutputName) {
			this.methodOutputName = methodOutputName;
			return this;
		}

		public String getMethodInputNameSpace() {
			return methodInputNameSpace;
		}

		public WsRpcActionData setMethodInputNameSpace(String methodInputNameSpace) {
			this.methodInputNameSpace = methodInputNameSpace;
			return this;
		}

		public String getMethodOutputNameSpace() {
			return methodOutputNameSpace;
		}

		public WsRpcActionData setMethodOutputNameSpace(String methodOutputNameSpace) {
			this.methodOutputNameSpace = methodOutputNameSpace;
			return this;
		}

		public String getRpcPartInput() {
			return rpcPartInput;
		}

		public WsRpcActionData setRpcPartInput(String rpcPartInput) {
			this.rpcPartInput = rpcPartInput;
			return this;
		}

		public String getRpcPartOutput() {
			return rpcPartOutput;
		}

		public WsRpcActionData setRpcPartOutput(String rpcPartOutput) {
			this.rpcPartOutput = rpcPartOutput;
			return this;
		}

	}

	public static WsRpcActionData getWsRpcActionData(Map<QName, String> props) {

		return new WsRpcActionData().setServiceName(props.get(new QName(SERVICE_NAME))).setMethodInputName(
				props.get(new QName(METHOD_INPUT_NAME))).setMethodOutputName(props.get(new QName(METHOD_OUTPUT_NAME))).setMethodInputNameSpace(
				props.get(new QName(METHOD_INPUT_NAMESPACE))).setMethodOutputNameSpace(
				props.get(new QName(METHOD_INPUT_NAMESPACE))).setRpcPartInput(
				props.getOrDefault(new QName(RPC_PART_INPUT), "|none|")).setRpcPartOutput(
				props.getOrDefault(new QName(RPC_PART_OUTPUT), "|none|"));
	}

}
