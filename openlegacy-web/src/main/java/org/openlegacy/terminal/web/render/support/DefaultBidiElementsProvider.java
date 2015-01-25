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
package org.openlegacy.terminal.web.render.support;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.terminal.Color;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalFieldSplitter;
import org.openlegacy.terminal.support.BidiTerminalFieldSplitter;
import org.openlegacy.web.HtmlConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.List;

public class DefaultBidiElementsProvider extends DefaultElementsProvider {

	@Override
	public Element createLabel(Element rootNode, TerminalField field, ScreenSize screenSize) {
		String visualValue = field.getValue();

		if (visualValue != null) {
			TerminalFieldSplitter splitter = new BidiTerminalFieldSplitter();
			List<TerminalField> fields = splitter.split(field, screenSize);
			if (fields == null) {
				return super.createLabel(rootNode, field, screenSize);
			} else {
				Element container = rootNode.getOwnerDocument().createElement("span");
				container.setAttribute("style", "position:relative");
				for (TerminalField splitField : fields) {
					Element node = createLabelInner(rootNode, splitField, screenSize);
					if (node != null) {
						container.appendChild(node);

					}
				}
				rootNode.appendChild(container);
				return container;
			}
		} else {
			return super.createLabel(rootNode, field, screenSize);
		}
	}

	private Element createLabelInner(Element rootNode, TerminalField field, ScreenSize screenSize) {
		String value = field.getVisualValue();
		if (value == null || value.trim().length() > 0) {
			value = field.getValue();
		}
		if (field.isHidden() || StringUtils.isWhitespace(value) && field.getBackColor() == Color.BLACK) {
			return null;
		}
		if (field.getBackColor() == Color.BLACK) {
			value = value.trim();
		}

		Element label = createTag(rootNode, HtmlConstants.SPAN);
		populateCommonAttributes(label, field);

		Text textNode = rootNode.getOwnerDocument().createTextNode(value);
		label.appendChild(textNode);

		rootNode.appendChild(label);
		return label;

	}
}
