package org.openlegacy.providers.wsrpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.providers.wsrpc.utils.FieldUtil;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFields;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.support.SimpleRpcInvokeAction;
import org.openlegacy.rpc.support.SimpleRpcResult;
import org.openlegacy.rpc.support.SimpleRpcStructureField;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;
import org.openlegacy.utils.WsRpcActionUtil;
import org.openlegacy.utils.WsRpcActionUtil.WsRpcActionData;

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class WsRpcConnection implements RpcConnection {

	public static class WsRpcConnectionRuntimeException extends OpenLegacyRuntimeException {

		private static final long serialVersionUID = 1L;

		public WsRpcConnectionRuntimeException(Exception exception) {
			super(exception);
		}
	}

	private static final Log logger = LogFactory.getLog(WsRpcConnection.class);

	private static final String ACTION_PREFIX = "prefix";
	private static final String RESPONSE = "Response";

	private static final String ELEMENT_FAULT = "Fault";
	private static final String ELEMENT_FAULT_CODE = "faultcode";
	private static final String ELEMENT_FAULT_STRING = "faultstring";

	private SOAPConnectionFactory connectionFactory;
	private MessageFactory messageFactory;
	private WsRpcActionData actionData;
	private String baseURL;

	public WsRpcConnection() {
		try {
			connectionFactory = SOAPConnectionFactory.newInstance();
			messageFactory = MessageFactory.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RpcSnapshot getSnapshot() {
		return null;
	}

	@Override
	public RpcSnapshot fetchSnapshot() {
		return null;
	}

	@Override
	public void doAction(RpcInvokeAction sendAction) {
		invoke(sendAction);
	}

	@Override
	public Integer getSequence() {
		return null;
	}

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public void disconnect() {

	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		SimpleRpcResult result = new SimpleRpcResult();
		try {
			SOAPConnection connection = connectionFactory.createConnection();
			soapToRpcResult(connection.call(actionToSoap(rpcInvokeAction), getServiceURL(rpcInvokeAction.getRpcPath())),
					rpcInvokeAction, result);
			connection.close();
		} catch (Exception e) {
			throw (new WsRpcConnectionRuntimeException(e));
		}

		return result;
	}

	@Override
	public void login(String user, String password) {

	}

	private SOAPMessage actionToSoap(RpcInvokeAction action) throws Exception {

		SOAPMessage message = messageFactory.createMessage();
		actionData = WsRpcActionUtil.getWsRpcActionData(((SimpleRpcInvokeAction)action).getProperties());

		SOAPEnvelope env = message.getSOAPPart().getEnvelope();
		env.addNamespaceDeclaration(ACTION_PREFIX, actionData.getMethodInputNameSpace());
		SOAPElement actionElement;
		if (actionData.getStyle().equals("rpc")) {
			actionElement = env.getBody().addChildElement(actionData.getMethodInputName(), ACTION_PREFIX);
		} else {
			actionElement = env.getBody();
		}

		setFields(action.getFields(), actionElement);

		if (actionData.getSoapAction().length() > 0) {
			message.getMimeHeaders().addHeader("SOAPAction", actionData.getSoapAction());
		}

		logMessage("Request message:", message);

		return message;
	}

	private void soapToRpcResult(SOAPMessage response, RpcInvokeAction action, SimpleRpcResult result) throws Exception {
		logMessage("Response message:", response);
		SOAPBody responseBody = response.getSOAPBody();

		checkForFailureReponse(responseBody);

		actionData = WsRpcActionUtil.getWsRpcActionData(((SimpleRpcInvokeAction)action).getProperties());

		if (actionData.getStyle().contains("rpc")) {
			QName responseQName = new QName(actionData.getMethodOutputNameSpace(), actionData.getMethodOutputName());
			getFields(action.getFields(), responseBody.getChildElements(responseQName));
		} else {
			getFields(action.getFields(), responseBody.getChildElements());
		}

		result.setRpcFields(action.getFields());
	}

	private boolean needToBreakGroup(RpcField field) {
		return field.getName().endsWith(WsRpcActionUtil.INPUT) || field.getName().endsWith(WsRpcActionUtil.OUTPUT)
				|| field.getName().endsWith(WsRpcActionUtil.INPUT_OUTPUT);
	}

	private void setFields(List<RpcField> fields, SOAPElement actionElement) throws SOAPException {
		for (RpcField field : fields) {
			if (field instanceof SimpleRpcStructureField) {
				if (field.getName().endsWith(WsRpcActionUtil.INPUT) || field.getName().endsWith(WsRpcActionUtil.INPUT_OUTPUT)) {
					setFields(((SimpleRpcStructureField)field).getChildrens(), actionElement);
				}

				if (needToBreakGroup(field)) {
					continue;
				}
			}

			if (field.getDirection() == Direction.INPUT || field.getDirection() == Direction.INPUT_OUTPUT) {
				if (FieldUtil.isPrimitive(field)) {
					if (actionData.getElementFormDefaultUse().equals("true")) {
						FieldUtil.writePrimitiveField((RpcFlatField)field,
								actionElement.addChildElement(field.getName(), ACTION_PREFIX));
					} else {
						FieldUtil.writePrimitiveField((RpcFlatField)field, actionElement.addChildElement(field.getName()));
					}
				} else if (field instanceof SimpleRpcStructureField) {
					if (actionData.getElementFormDefaultUse().equals("true")) {
						setFields(((SimpleRpcStructureField)field).getChildrens(),
								actionElement.addChildElement(field.getLegacyContainerName()));
					} else {
						setFields(((SimpleRpcStructureField)field).getChildrens(),
								actionElement.addChildElement(field.getLegacyContainerName(), ACTION_PREFIX));
					}
					setFields(((SimpleRpcStructureField)field).getChildrens(), actionElement.addChildElement(
							field.getLegacyContainerName(), actionData.getElementFormDefaultUse().equals("true") ? ACTION_PREFIX
									: ""));
				} else if (field instanceof SimpleRpcStructureListField) {
					List<RpcFields> children = ((SimpleRpcStructureListField)field).getChildrens();
					for (int i = 0; i < children.size(); i++) {
						setFields(children.get(i).getFields(), actionElement);
					}
				}
			}
		}
	}

	private void getFields(List<RpcField> fields, Iterator<?> parent) throws Exception {
		for (RpcField field : fields) {
			if (field instanceof SimpleRpcStructureField) {
				if (field.getName().endsWith(WsRpcActionUtil.OUTPUT) || field.getName().endsWith(WsRpcActionUtil.INPUT_OUTPUT)) {
					getFields(((SimpleRpcStructureField)field).getChildrens(), parent);
				}

				if (needToBreakGroup(field)) {
					continue;
				}
			}

			if (field.getDirection() == Direction.OUTPUT || field.getDirection() == Direction.INPUT_OUTPUT) {
				if (field.getDirection() == Direction.OUTPUT || field.getDirection() == Direction.INPUT_OUTPUT) {
					String elementName = field.getLegacyContainerName() != null ? field.getLegacyContainerName()
							: field.getName();
					SOAPElement data = findSOAPElement(parent, elementName);

					if (data == null) {
						throw new Exception(String.format("SOAPElement with name \" %s \" is unfound", field.getName()));
					}

					if (FieldUtil.isPrimitive(field)) {
						FieldUtil.readPrimitiveField((RpcFlatField)field, data);
					} else if (field instanceof SimpleRpcStructureField) {
						getFields(((SimpleRpcStructureField)field).getChildrens(), data.getChildElements());
					} else if (field instanceof SimpleRpcStructureListField) {
						List<RpcFields> children = ((SimpleRpcStructureListField)field).getChildrens();
						for (int i = 0; i < children.size(); i++) {
							getFields(children.get(i).getFields(), data.getChildElements());
							data = findSOAPElement(parent, elementName);
						}
					}
				}
			}
		}

	}

	private void logMessage(String header, SOAPMessage message) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(header);
				message.writeTo(System.out);
				System.out.println();
			}
		} catch (Exception e) {
		}
	}

	private SOAPElement findSOAPElement(Iterator<?> parent, String name) {
		while (parent.hasNext()) {
			SOAPElement child, element;
			Object test = parent.next();

			if (!(test instanceof SOAPElement)) { // for correct element search
				continue;
			}

			element = (SOAPElement)test;

			if (element.getElementName().getLocalName().equals(name)) {
				child = element;
			} else {
				child = findSOAPElement(element.getChildElements(), name);
			}

			if (child != null) { // without it only first cycle iteration will proceed
				return child;
			}
		}
		return null;
	}

	// currently designed for sample service implementation
	private void checkForFailureReponse(SOAPBody responseBody) throws Exception {
		SOAPElement faultElement = findSOAPElement(responseBody.getChildElements(), ELEMENT_FAULT);

		if (faultElement == null) {
			return;
		}

		SOAPElement faultCode, faultString;
		faultCode = findSOAPElement(faultElement.getChildElements(), ELEMENT_FAULT_CODE);
		faultString = findSOAPElement(faultElement.getChildElements(), ELEMENT_FAULT_STRING);

		throw new Exception(String.format("%s>> %s", faultCode.getValue(), faultString.getValue()));
	}

	public void setBaseUrl(String baseURL) {
		this.baseURL = baseURL.endsWith("/") ? baseURL.substring(0, baseURL.length() - 1) : baseURL;
	}

	private String getServiceURL(String rpcPath) {
		return baseURL + rpcPath;
	}
}
