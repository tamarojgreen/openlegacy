package org.openlegacy.providers.wsrpc;

import org.apache.commons.collections.IteratorUtils;
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
import org.openlegacy.rpc.support.SimpleRpcFlatField;
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
import javax.xml.soap.SOAPHeaderElement;
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

	private static final String ELEMENT_FAULT = "Fault";
	private static final String ELEMENT_FAULT_CODE = "faultcode";
	private static final String ELEMENT_FAULT_STRING = "faultstring";

	private SOAPConnectionFactory connectionFactory;
	private MessageFactory messageFactory;
	private WsRpcActionData actionData;
	private UrlProps props;
	private boolean useFirstElementPrefix = true;

	private enum FieldType {
		simple,
		structure,
		list
	};

	public WsRpcConnection() {
		try {
			connectionFactory = SOAPConnectionFactory.newInstance();
			messageFactory = MessageFactory.newInstance();
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}

	public WsRpcConnection(UrlProps props) {
		this();
		this.props = props;
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
		useFirstElementPrefix = true;

		SOAPMessage message = messageFactory.createMessage();
		actionData = WsRpcActionUtil.getWsRpcActionData(((SimpleRpcInvokeAction) action).getProperties());

		SOAPEnvelope env = message.getSOAPPart().getEnvelope();
		env.addNamespaceDeclaration(ACTION_PREFIX, actionData.getMethodInputNameSpace());

		setFields(action.getFields(), env.getBody());

		if (actionData.getSoapAction().length() > 0) {
			message.getMimeHeaders().addHeader("SOAPAction", actionData.getSoapAction());
		}

		// Base auth headers from http://www.whitemesa.com/soapauth.html#S322
		/*
		 * all auth standarts: http://www.soapui.org/testing-dojo/best-practices/authentication.html
		 */
		if (props.getPassword() != null && props.getUserName() != null && !props.getUserName().trim().equals("")) {
			SOAPHeaderElement auth = message.getSOAPHeader().addHeaderElement(
					new QName("http://soap-authentication.org/basic/2001/10/", "BasicAuth", "h"));
			auth.setMustUnderstand(true);
			auth.addChildElement("Name").setValue(props.getUserName());
			auth.addChildElement("Password").setValue(props.getPassword());
		}

		logMessage("Request message:", message);

		return message;
	}

	private void soapToRpcResult(SOAPMessage response, RpcInvokeAction action, SimpleRpcResult result) throws Exception {
		logMessage("Response message:", response);
		SOAPBody responseBody = response.getSOAPBody();
		checkForFailureReponse(responseBody);
		actionData = WsRpcActionUtil.getWsRpcActionData(((SimpleRpcInvokeAction) action).getProperties());
		getFields(action.getFields(), responseBody.getChildElements());
		result.setRpcFields(action.getFields());
	}

	private void setFields(List<RpcField> fields, SOAPElement actionElement) throws SOAPException {
		boolean elementFormDefault = actionData.getElementFormDefaultUse().equals("true");

		for (RpcField field : fields) {
			if (field.getDirection() == Direction.OUTPUT) {
				continue;
			}

			if (field.getName().equals(WsRpcActionUtil.FORCED_CHILD)) {
				continue;
			}

			SOAPElement element;
			switch (getFieldType(field)) {
				case simple:
					RpcFlatField flatField = (RpcFlatField) field;
					element = elementFormDefault ? actionElement.addChildElement(flatField.getOriginalName(), ACTION_PREFIX)
							: actionElement.addChildElement(flatField.getOriginalName());
					FieldUtil.writePrimitiveField((RpcFlatField) field, element);
					break;
				case structure:
					SimpleRpcStructureField structureField = (SimpleRpcStructureField) field;
					String soapElement = structureField.getOriginalName();

					element = (elementFormDefault || useFirstElementPrefix) ? actionElement.addChildElement(soapElement,
							ACTION_PREFIX) : actionElement.addChildElement(soapElement);
					useFirstElementPrefix = false;
					if (structureField.getExpandedElements() != null) {
						for (String expected : structureField.getExpandedElements()) {
							element = elementFormDefault ? element.addChildElement(expected, ACTION_PREFIX)
									: element.addChildElement(expected);
						}
					}
					setFields(structureField.getChildrens(), element);
					break;
				case list:
					SimpleRpcStructureListField listField = (SimpleRpcStructureListField) field;
					if (listField.getOriginalName() != null) {
						if (!listField.getOriginalName().equals("")) {
							String listOriginalName = listField.getOriginalNameForList();
							actionElement = (elementFormDefault || useFirstElementPrefix) ? actionElement.addChildElement(
									listOriginalName, ACTION_PREFIX) : actionElement.addChildElement(listOriginalName);
							useFirstElementPrefix = false;
						}
					}
					List<RpcFields> childrens = listField.getChildrens();
					for (int i = 0; i < childrens.size(); i++) {
						element = (elementFormDefault || useFirstElementPrefix) ? actionElement.addChildElement(
								listField.getOriginalName(), ACTION_PREFIX)
								: actionElement.addChildElement(listField.getOriginalName());
						useFirstElementPrefix = false;
						setFields(childrens.get(i).getFields(), element);
					}
					break;
			}
		}
	}

	private void logAndRemoveElementFromList(SOAPElement element, String name, List<?> elements) {
		if (element == null) {
			logger.error(String.format("Element with %s name is unfound", name));
		} else {
			logger.info(String.format("Processing %s element", name));
			elements.remove(element);
		}

	}

	private void getFields(List<RpcField> fields, Iterator<?> parent) throws Exception {
		List<?> convertedIterator = null;

		SOAPElement element;
		for (RpcField field : fields) {
			if (field.getDirection() == Direction.INPUT) {
				continue;
			}

			if (field.getName().equals(WsRpcActionUtil.FORCED_CHILD)) {
				continue;
			}

			if (convertedIterator == null) {
				convertedIterator = IteratorUtils.toList(parent);
			}

			switch (getFieldType(field)) {
				case simple:
					RpcFlatField flatField = (RpcFlatField) field;
					element = findSOAPElement(convertedIterator, flatField.getOriginalName());
					logAndRemoveElementFromList(element, flatField.getOriginalName(), convertedIterator);

					if (element == null) {
						continue;
					}

					FieldUtil.readPrimitiveField(flatField, element);
					break;
				case structure:
					SimpleRpcStructureField structureField = (SimpleRpcStructureField) field;
					element = findSOAPElement(convertedIterator, structureField.getOriginalName());
					logAndRemoveElementFromList(element, structureField.getOriginalName(), convertedIterator);

					if (element == null) {
						continue;
					}

					if (structureField.getExpandedElements() != null) {
						for (String expanded : structureField.getExpandedElements()) {
							List<?> localConvertedIterator = IteratorUtils.toList(element.getChildElements());
							element = findSOAPElement(localConvertedIterator, expanded);
							logAndRemoveElementFromList(element, expanded, localConvertedIterator);

							if (element == null) {
								break;
							}
						}
					}

					if (element == null) {
						continue;
					}
					getFields(structureField.getChildrens(), element.getChildElements());
					break;
				case list:
					SimpleRpcStructureListField listField = (SimpleRpcStructureListField) field;
					if (listField.getOriginalName() != null) {
						if (!listField.getOriginalName().equals("")) {
							element = findSOAPElement(convertedIterator, listField.getOriginalNameForList());
							logAndRemoveElementFromList(element, listField.getOriginalNameForList(), convertedIterator);
							if (element != null) {
								convertedIterator = IteratorUtils.toList(element.getChildElements());
							}
						}
					}

					List<RpcFields> childrens = listField.getChildrens();

					for (int i = 0; i < childrens.size(); i++) {
						element = findSOAPElement(convertedIterator, listField.getOriginalName());
						logAndRemoveElementFromList(element, listField.getOriginalName(), convertedIterator);
						if (element == null) {
							continue;
						}
						getFields(childrens.get(i).getFields(), element.getChildElements());
					}
					break;
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

			element = (SOAPElement) test;

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

	private SOAPElement findSOAPElement(List<?> parentData, String name) {
		for (Object obj : parentData) {
			if (!(obj instanceof SOAPElement)) {
				continue;
			}
			SOAPElement child, element = (SOAPElement) obj;
			if (element.getElementName().getLocalName().equals(name)) {
				child = element;
			} else {
				child = findSOAPElement(IteratorUtils.toList(element.getChildElements()), name);
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

	private String getServiceURL(String rpcPath) {
		return props.getBaseUrl() + rpcPath;
	}

	private FieldType getFieldType(RpcField field) {
		if (field instanceof SimpleRpcFlatField) {
			return FieldType.simple;
		} else {
			return field instanceof SimpleRpcStructureField ? FieldType.structure : FieldType.list;
		}
	}
}