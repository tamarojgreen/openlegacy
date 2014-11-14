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
package org.openlegacy.web;

import org.apache.commons.beanutils.PropertyUtils;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.modules.table.TableWriter;
import org.openlegacy.utils.DomUtils;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.TypesUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

public class DefaultHtmlTableWriter implements TableWriter {

	@Override
	public void writeTable(List<? extends Object> records, TableDefinition<ColumnDefinition> tableDefinition,
			OutputStream outputStream) {
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();

		Document doc;
		try {
			doc = dfactory.newDocumentBuilder().newDocument();

			Element tableTag = (Element)doc.appendChild(doc.createElement(HtmlConstants.TABLE));

			if (records.size() == 0) {
				return;
			}

			Object firstRecord = records.get(0);
			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(firstRecord);

			// render headers with display name of the fields
			Element rowTag = createTag(tableTag, HtmlConstants.TR);
			for (PropertyDescriptor propertyDescriptor : descriptors) {
				if (TypesUtil.isPrimitive(propertyDescriptor.getPropertyType())) {
					Element headerTag = createTag(rowTag, HtmlConstants.TH);
					String displayName = "";
					if (tableDefinition == null) {
						displayName = StringUtil.toDisplayName(propertyDescriptor.getName());
					} else {
						displayName = tableDefinition.getColumnDefinition(propertyDescriptor.getName()).getDisplayName();
					}

					setCellValue(headerTag, displayName);
				}

			}

			for (Object object : records) {
				rowTag = createTag(tableTag, HtmlConstants.TR);
				for (PropertyDescriptor propertyDescriptor : descriptors) {
					if (TypesUtil.isPrimitive(propertyDescriptor.getPropertyType())) {
						Element cellTag = createTag(rowTag, HtmlConstants.TD);
						Object value = propertyDescriptor.getReadMethod().invoke(object);
						if (value == null) {
							value = "";
						}
						setCellValue(cellTag, String.valueOf(value));
					}
				}
			}

			DomUtils.render(doc, outputStream);
		} catch (Exception e) {
			throw (new GenerationException(e));
		}

	}

	private static void setCellValue(Element cellTag, String value) {
		Document doc = cellTag.getOwnerDocument();
		cellTag.appendChild(doc.createTextNode(value));

	}

	private static Element createTag(Element rootNode, String tagName) {
		Element tag = rootNode.getOwnerDocument().createElement(tagName);
		rootNode.appendChild(tag);
		return tag;
	}

}
