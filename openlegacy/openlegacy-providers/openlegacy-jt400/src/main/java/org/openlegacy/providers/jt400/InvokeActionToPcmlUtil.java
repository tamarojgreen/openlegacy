/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.providers.jt400;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class InvokeActionToPcmlUtil {

	private final String ROOT_NODE = "pcml";
	private final String VERSION_ATTR_NAME = "version";
	private final String VERSION_ATTR_VALUE = "4.0";
	private final String STRUCTURE_NODE = "struct";
	private final String DATA_NODE = "data";
	private final String NAME_ATTR_NAME = "name";
	private final String TYPE_ATTR_NAME = "type";
	private final String LENGTH_ATTR_NAME = "length";
	private final String USAGE_ATTR_NAME = "usage";
	private final String PROGAME_NODE = "program";
	private final String PATH_ATTR_NAME = "path";
	private final String STRCTURE_POSTFIX = "_structure";
	private final String INITIAL_ATTR_NAME = "init";
	private final String COUNT_ATTR_NAME = "count";

	// static private Map<String, String> typeTable = new HashMap<String, String>();
	static private Map<Direction, String> directionTable = new HashMap<Direction, String>();

	InvokeActionToPcmlUtil() {
		// typeTable.put("String", "char");
		// typeTable.put("Byte", "char");
		// typeTable.put("Short", "char");
		// typeTable.put("Integer", "char");
		// typeTable.put("Long", "char");
		// typeTable.put("BigDecimal", "packed");
		// typeTable.put("Float", "float");
		// typeTable.put("Double", "float");
		// typeTable.put("Date", "char");
		// typeTable.put("Boolean", "char");

		directionTable.put(Direction.INPUT, "input");
		directionTable.put(Direction.INPUT_OUTPUT, "inputoutput");
		directionTable.put(Direction.OUTPUT, "output");
	}

	public byte[] serilize(RpcInvokeAction rpcInvokeAction, String pcmlName) throws ParserConfigurationException,
			TransformerException {
		Document doc = work(rpcInvokeAction, pcmlName);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		StreamResult streamResult = new StreamResult(outputStream);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(source, streamResult);
		return outputStream.toByteArray();
	}

	public void save(RpcInvokeAction rpcInvokeAction, String dir, String pcmlName) throws ParserConfigurationException,
			TransformerException {

		Document doc = work(rpcInvokeAction, pcmlName);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		transformer.transform(source, new StreamResult(new File(dir + pcmlName + ".pcml")));

	}

	private Document work(RpcInvokeAction rpcInvokeAction, String pcmlName) throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(ROOT_NODE);
		rootElement.setAttribute(VERSION_ATTR_NAME, VERSION_ATTR_VALUE);
		doc.appendChild(rootElement);

		List<org.openlegacy.rpc.RpcField> rpcFields = rpcInvokeAction.getFields();
		handleStruct(doc, rootElement, rpcFields, true);

		Element programElement = doc.createElement(PROGAME_NODE);
		rootElement.appendChild(programElement);
		programElement.setAttribute(NAME_ATTR_NAME, pcmlName);
		programElement.setAttribute(PATH_ATTR_NAME, rpcInvokeAction.getRpcPath());

		for (org.openlegacy.rpc.RpcField rpcField : rpcFields) {

			if (rpcField instanceof RpcStructureField || rpcField instanceof RpcStructureListField) {
				Element programDataElement = doc.createElement(DATA_NODE);
				programDataElement.setAttribute(TYPE_ATTR_NAME, STRUCTURE_NODE);
				programDataElement.setAttribute(NAME_ATTR_NAME, rpcField.getName());
				programDataElement.setAttribute(STRUCTURE_NODE, rpcField.getName() + STRCTURE_POSTFIX);
				programDataElement.setAttribute(USAGE_ATTR_NAME, directionTable.get(rpcField.getDirection()));

				programElement.appendChild(programDataElement);
			} else {
				programElement.appendChild(createFlatField(doc, (RpcFlatField)rpcField));
			}
		}
		return doc;
	}

	private void handleStruct(Document doc, Element rootElement, List<org.openlegacy.rpc.RpcField> rpcFields, Boolean top) {
		for (org.openlegacy.rpc.RpcField rpcField : rpcFields) {

			if (rpcField instanceof RpcStructureField || rpcField instanceof RpcStructureListField) {

				Element struct = doc.createElement(STRUCTURE_NODE);
				rootElement.appendChild(struct);
				if (top) {
					struct.setAttribute(NAME_ATTR_NAME, rpcField.getName() + STRCTURE_POSTFIX);
				} else {
					struct.setAttribute(NAME_ATTR_NAME, rpcField.getName());
				}
				if (rpcField instanceof RpcStructureListField) {
					struct.setAttribute(COUNT_ATTR_NAME, ((RpcStructureListField)rpcField).count().toString());
					handleStruct(doc, struct, ((RpcStructureListField)rpcField).getChildren(0), false);
				} else {
					handleStruct(doc, struct, ((RpcStructureField)rpcField).getChildrens(), false);
				}
			} else {
				if (top == false) {
					rootElement.appendChild(createFlatField(doc, (RpcFlatField)rpcField));
				}
			}

		}

	}

	private Element createFlatField(Document doc, org.openlegacy.rpc.RpcFlatField rpcFlatField) {
		Element struct = doc.createElement(DATA_NODE);
		struct.setAttribute(NAME_ATTR_NAME, rpcFlatField.getName());
		struct.setAttribute(TYPE_ATTR_NAME, "char");
		struct.setAttribute(LENGTH_ATTR_NAME, rpcFlatField.getLength().toString());
		struct.setAttribute(USAGE_ATTR_NAME, directionTable.get(rpcFlatField.getDirection()));
		if (rpcFlatField.getDefaultValue() != null && !("".equals(rpcFlatField.getDefaultValue()))) {
			struct.setAttribute(INITIAL_ATTR_NAME, rpcFlatField.getDefaultValue().toString());
		}
		// if (!rpcFlatField.getCount().equals(1)) {
		// struct.setAttribute(COUNT_ATTR_NAME, rpcFlatField.getCount().toString());
		// }

		return struct;
	}
}
