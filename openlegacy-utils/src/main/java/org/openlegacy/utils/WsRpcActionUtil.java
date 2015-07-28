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

	public static final String INPUT = "InputParams";
	public static final String OUTPUT = "OutputParams";
	public static final String IN = "In";
	public static final String OUT = "Out";
	public static final String INPUT_OUTPUT = "InputOutputParams";

	public static final String OPERATION_PROTOCOL = "operationProtocol";
	public static final String OPERATION = "operation";

	public static final String ELEMENT_FORM_DEFAULT_USE = "elementFormDefaultUse";
	public static final String STYLE = "style";
	public static final String SOAP_ACTION = "soapAction";
	public static final String FORCED_CHILD = "forcedChild";

	public static class WsRpcActionData {

		String targetNamespace, serviceName, methodInputName, methodOutputName, methodInputNameSpace, methodOutputNameSpace,
				elementFormDefaultUse, style, soapAction;

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

		public String getElementFormDefaultUse() {
			return elementFormDefaultUse != null ? elementFormDefaultUse : "false";
		}

		public WsRpcActionData setElementFormDefaultUse(String elementFormDefaultUse) {
			this.elementFormDefaultUse = elementFormDefaultUse;
			return this;
		}

		public String getStyle() {
			return style;
		}

		public WsRpcActionData setStyle(String style) {
			this.style = style;
			return this;
		}

		public String getSoapAction() {
			return soapAction;
		}

		public WsRpcActionData setSoapAction(String soapAction) {
			this.soapAction = soapAction;
			return this;
		}

	}

	public static WsRpcActionData getWsRpcActionData(Map<QName, String> props) {

		return new WsRpcActionData().setServiceName(props.get(new QName(SERVICE_NAME))).setTargetNamespace(
				props.get(new QName(TARGET_NAMESPACE))).setMethodInputName(props.get(new QName(METHOD_INPUT_NAME))).setMethodOutputName(
				props.get(new QName(METHOD_OUTPUT_NAME))).setMethodInputNameSpace(props.get(new QName(METHOD_INPUT_NAMESPACE))).setMethodOutputNameSpace(
				props.get(new QName(METHOD_INPUT_NAMESPACE))).setElementFormDefaultUse(
				props.get(new QName(ELEMENT_FORM_DEFAULT_USE))).setStyle(props.get(new QName(STYLE))).setSoapAction(
				props.get(new QName(SOAP_ACTION)));
	}
}
