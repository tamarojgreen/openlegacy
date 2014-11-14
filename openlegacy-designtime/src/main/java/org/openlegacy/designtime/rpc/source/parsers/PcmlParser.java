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
package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;
import org.openlegacy.designtime.rpc.model.support.SimpleRpcEntityDesigntimeDefinition;
import org.openlegacy.designtime.rpc.source.CodeParser;
import org.openlegacy.rpc.actions.RpcActions;
import org.openlegacy.rpc.actions.RpcActions.READ;
import org.openlegacy.rpc.definitions.RpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcActionDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcFieldDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcPartEntityDefinition;
import org.openlegacy.utils.StringUtil;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PcmlParser implements CodeParser {

	@Override
	public ParseResults parse(String source, String fileName) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(source.getBytes()));
			List<Element> structures = DomUtils.getChildElementsByTagName(doc.getDocumentElement(), "struct");

			SimpleRpcEntityDesigntimeDefinition rpcDefinition = new SimpleRpcEntityDesigntimeDefinition();
			for (Element structureElement : structures) {
				String partName = StringUtil.toClassName(structureElement.getAttribute("name"));
				SimpleRpcPartEntityDefinition partEntityDefinition = new SimpleRpcPartEntityDefinition(null);
				partEntityDefinition.setPartName(partName);
				rpcDefinition.getPartsDefinitions().put(partName, partEntityDefinition);
				Map<String, RpcFieldDefinition> fieldsDefinitions = partEntityDefinition.getFieldsDefinitions();
				createFields(structureElement, fieldsDefinitions);
			}
			List<Element> programs = DomUtils.getChildElementsByTagName(doc.getDocumentElement(), "program");
			for (Element programElement : programs) {
				rpcDefinition.setEntityName(StringUtil.toClassName(programElement.getAttribute("name")));

				SimpleRpcActionDefinition actionDefinition = new SimpleRpcActionDefinition(RpcActions.READ(),
						StringUtil.toDisplayName(READ.class.getSimpleName()));
				actionDefinition.setProgramPath(programElement.getAttribute("path"));
				rpcDefinition.getActions().add(actionDefinition);
				Map<String, RpcFieldDefinition> fieldsDefinitions = rpcDefinition.getFieldsDefinitions();
				createFields(programElement, fieldsDefinitions);
			}

			return new SimpleParseResults(rpcDefinition);
		} catch (Exception e) {
			throw (new OpenLegacyParseException(e.getMessage(), e));
		}
	}

	private static void createFields(Element containerElement, Map<String, RpcFieldDefinition> fieldsDefinitions) {
		List<Element> fields = DomUtils.getChildElementsByTagName(containerElement, "data");
		for (Element fieldElement : fields) {
			if (fieldElement.getAttribute("type").equals("struct")) {
				continue;
			}
			RpcFieldDefinition fieldDefinition = createField(fieldElement);
			fieldsDefinitions.put(fieldDefinition.getName(), fieldDefinition);
		}
	}

	private static RpcFieldDefinition createField(Element fieldElement) {
		String originalName = fieldElement.getAttribute("name");
		String fieldName = StringUtil.toJavaFieldName(originalName);
		SimpleRpcFieldDefinition fieldDefinition = new SimpleRpcFieldDefinition(fieldName, null);
		String usage = fieldElement.getAttribute("usage");
		if (usage.equals("input")) {
			fieldDefinition.setDirection(Direction.INPUT);
		} else if (usage.equals("output")) {
			fieldDefinition.setDirection(Direction.OUTPUT);
		} else {
			fieldDefinition.setDirection(Direction.INPUT_OUTPUT);
		}
		fieldDefinition.setJavaType(String.class);
		fieldDefinition.setOriginalName(originalName);
		fieldDefinition.setFieldTypeDefinition(new SimpleTextFieldTypeDefinition());
		// fieldDefinition.setFieldTypeName(General.class.getSimpleName());
		String lengthStr = fieldElement.getAttribute("length");
		if (!StringUtil.isEmpty(lengthStr)) {
			fieldDefinition.setLength(Integer.valueOf(lengthStr));
		} else {
			fieldDefinition.setLength(0);
		}
		return fieldDefinition;
	}

	@Override
	public ParseResults parse(String source, Map<String, InputStream> streamMap) throws IOException {
		return null;
	}

}
